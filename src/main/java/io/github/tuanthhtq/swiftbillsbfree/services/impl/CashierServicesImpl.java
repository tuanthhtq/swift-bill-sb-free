package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.payment.*;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ProductInfoResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ProductSearchCriteria;
import io.github.tuanthhtq.swiftbillsbfree.entities.*;
import io.github.tuanthhtq.swiftbillsbfree.repositories.BankAccountsRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.ProductsRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.ReceiptsRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.UsersRepository;
import io.github.tuanthhtq.swiftbillsbfree.services.CashierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author io.github.tuanthhtq
 */

@Service
public class CashierServicesImpl extends CommonConcrete implements CashierServices {


	private final RestTemplate restTemplate;
	private final UsersRepository usersRepo;
	private final ProductsRepository productsRepo;
	private final ReceiptsRepository receiptsRepo;
	private final BankAccountsRepository bankRepo;

	@Value("${app.basic-qrcode-base-url}")
	private String qrBaseUrl;

	@Value("${app.basic-qrcode-template-id}")
	private String qrTemplateId;

	@Autowired
	public CashierServicesImpl(
			RestTemplate restTemplate,
			UsersRepository usersRepo,
			ProductsRepository productsRepo,
			ReceiptsRepository receiptsRepo,
			BankAccountsRepository bankRepo
	) {
		this.restTemplate = restTemplate;
		this.usersRepo = usersRepo;
		this.productsRepo = productsRepo;
		this.receiptsRepo = receiptsRepo;
		this.bankRepo = bankRepo;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<ProductInfoResponse> getProductInfo(ProductSearchCriteria request) {
		Response<ProductInfoResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Cannot find product");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());

		//check session
		Users user = getSessionUser(usersRepo);

		//check store
		Stores store = getSessionStore(usersRepo, request.storeId());

		if (user == null) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			errors.add("Invalid session");
			response.setErrors(errors);
		} else if (store == null) {
			response.setStatusCode(HttpStatus.FORBIDDEN.value());
			errors.add("Error getting store information, please select another store or create new one");
			response.setErrors(errors);
		} else {

			//find product by barcode or product id
			Products product = store.getProducts()
					.stream()
					.filter(item -> request.barcode() == null ?
							item.getId().equals(request.productId()) :
							item.getBarcode().equals(request.barcode()))
					.findFirst()
					.orElse(null);

			//check product
			if (product == null) {
				response.setMessage("Product not found, check barcode or store");
				errors.add("Invalid barcode");
				response.setErrors(errors);
			} else {
				response.setStatusCode(HttpStatus.OK.value());
				response.setMessage(product.getName());

				ProductInfoResponse responseData = new ProductInfoResponse(
						product.getId(),
						product.getName(),
						product.getBrand().getName(),
						product.getImages().stream().map(Images::getUrl).collect(Collectors.toSet()),
						product.getPrice()
				);

				response.setData(responseData);
			}
		}
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<PaymentResponse> createBill(PaymentRequest request, BindingResult bindingResult) {
		Response<PaymentResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Failed to create bill");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());

		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
				response.setErrors(errors);
			}
			return response;
		} else {
			//check user and store
			Users user = getSessionUser(usersRepo);
			Stores store = getSessionStore(usersRepo, request.storeId());

			if (user == null) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
				errors.add("Invalid session");
				response.setErrors(errors);
				return response;
			} else if (store == null) {
				response.setStatusCode(HttpStatus.FORBIDDEN.value());
				errors.add("Error getting store information, please select another store");
				response.setErrors(errors);
				return response;
			} else {

				//get bank
				BankAccounts bank = bankRepo.findById(request.bankAccountId()).orElse(null);

				if (bank == null) {
					response.setStatusCode(HttpStatus.BAD_REQUEST.value());
					errors.add("Please select another bank account");
					response.setErrors(errors);
				} else {
					//create new bill
					Receipts receipt = new Receipts();

					//create response data
					Set<PaymentItem> itemResponse = new HashSet<>();

					//create entity
					String orderCode = store.getStoreName() + System.currentTimeMillis();
					Set<Products> items = new HashSet<>();
					float amount = 0;

					//add product list
					for (PaymentItem item : request.items()) {
						Products product = productsRepo.findByBarcode(item.barcode()).orElse(null);
						if (product == null) {
							errors.add("Invalid request");
							response.setErrors(errors);
							return response;
						} else {
							//total amount
							amount += product.getPrice() * item.amount();
							items.add(product);

							//add item to response data
							itemResponse.add(new PaymentItem(
									product.getBarcode(),
									item.amount(),
									product.getName(),
									product.getMeasureUnit().getName(),
									product.getPrice()
							));
						}
					}


					//qrcode
					String imageName = String.format(
							"%s-%s-%s.jpg",
							bank.getBankCode(),
							bank.getAccountNumber(),
							qrTemplateId
					);
					String data = String.format(
							"accountName=%s&amount=%s&addInfo=%s",
							bank.getCardHolderName(),
							amount,
							store.getStoreName() + "Payment"

					);
					String qrCode = qrBaseUrl + imageName + "?" + data;

					//set data
					receipt.setOrderCode(orderCode);
					receipt.setCreator(user);
					receipt.setCustomerName(request.customerName());
					receipt.setCustomerPhone(request.customerPhone());
					receipt.setAmount(amount);
					receipt.setBank(bank);
					receipt.setItems(items);
					receipt.setStore(store);
					receipt.setQrCode(qrCode);

					//persist data
					Receipts saved = receiptsRepo.saveAndFlush(receipt);

					//set response data
					PaymentResponse responseData = new PaymentResponse(
							qrCode,
							bank.getAccountNumber(),
							bank.getCardHolderName(),
							saved.getCreatedDate(),
							itemResponse
					);

					response.setData(responseData);
					response.setStatusCode(HttpStatus.CREATED.value());
					response.setMessage("Created");
				}
				return response;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<PaymentResponse> confirmPayment(String orderCode) {
		Response<PaymentResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage("Failed to update receipt");

		//check user
		Users user = getSessionUser(usersRepo);

		if (user == null) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			errors.add("Invalid session");
			response.setErrors(errors);
			return response;
		} else {
			Receipts receipt = receiptsRepo.findByOrderCode(orderCode).orElse(null);

			if (receipt == null) {
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				errors.add("Invalid receipt");
				response.setErrors(errors);
				return response;
			} else {

				//update
				receipt.setPaid(true);
				receipt.setPaymentDate(new Date().toInstant());

				receiptsRepo.saveAndFlush(receipt);

				//response data
				PaymentResponse responseData = new PaymentResponse(
						receipt.getQrCode(),
						receipt.getBank().getAccountNumber(),
						receipt.getBank().getCardHolderName(),
						receipt.getCreatedDate(),
						receipt.getItems()
								.stream().map(item -> new PaymentItem(
										item.getBarcode(),
										item.
								))
				)
			}

		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<ReceiptDetail> getReceiptDetail(String orderCode) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<List<ReceiptInfo>> searchReceipts(ReceiptSearchCriteria request, BindingResult bindingResult) {
		return null;
	}


}
