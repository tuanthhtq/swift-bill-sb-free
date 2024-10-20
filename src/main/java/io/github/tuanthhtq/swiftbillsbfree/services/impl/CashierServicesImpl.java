package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.constants.Regex;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

	@Value("${app.default-page-size}")
	private int pageSize;

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

		try{
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
		}catch (Exception e){
			System.out.println(e.getLocalizedMessage());
			errors.add("Invalid request");
			response.setErrors(errors);
			return response;
		}
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

			try{
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
						//Steps
						// . Create ReceiptProducts set
						// . Create receipt
						// . Assign receiptProducts to receipt
						// . Persist receipt

						//order code
						String orderCode = store.getStoreName() + System.currentTimeMillis();

						//total quantity
						float amount = 0;

						//Create receiptProducts
						Set<ReceiptProducts> receiptProducts = new HashSet<>();
						for (PaymentItem i : request.items()){
							Products product = productsRepo.findByBarcode(i.barcode()).orElse(null);

							if(product == null){
								errors.add("Invalid request");
								response.setErrors(errors);
								return response;
							}else{
								//add to amount
								amount += i.unitPrice() * i.quantity();

								ReceiptProducts p = new ReceiptProducts();
								p.setQuantity(i.quantity());
								p.setProduct(product);
								p.setQuantity(i.quantity());
								p.setUnitPrice(i.unitPrice());

								receiptProducts.add(p);
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
								"accountName=%s&quantity=%s&addInfo=%s",
								bank.getCardHolderName(),
								amount,
								store.getStoreName() + "Payment"

						);
						String qrCode = qrBaseUrl + imageName + "?" + data;


						//Create receipt
						Receipts receipt = new Receipts();
						receipt.setOrderCode(orderCode);
						receipt.setCreator(user);
						receipt.setCustomerName(request.customerName());
						receipt.setCustomerPhone(receipt.getCustomerPhone());
						receipt.setAmount(amount);
						receipt.setQrCode(qrCode);
						receipt.setBank(bank);
						receipt.setItems(receiptProducts);
						receipt.setStore(store);

						//save new receipt
						Receipts savedReceipt = receiptsRepo.save(receipt);

						//create response data
						Set<PaymentItem> itemResponse = new HashSet<>();
						//set response data
						PaymentResponse responseData = new PaymentResponse(
								qrCode,
								bank.getAccountNumber(),
								bank.getCardHolderName(),
								savedReceipt.getCreatedDate(),
								itemResponse
						);

						response.setData(responseData);
						response.setStatusCode(HttpStatus.CREATED.value());
						response.setMessage("Created");
					}
					return response;
				}
			}catch (Exception e){
				System.out.println(e.getLocalizedMessage());
				errors.add("Invalid request");
				response.setErrors(errors);
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

		try{
			//check user
			Users user = getSessionUser(usersRepo);

			if (user == null) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
				errors.add("Invalid session");
				response.setErrors(errors);
			} else {
				Receipts receipt = receiptsRepo.findByOrderCode(orderCode).orElse(null);

				if (receipt == null) {
					response.setStatusCode(HttpStatus.BAD_REQUEST.value());
					errors.add("Invalid receipt");
					response.setErrors(errors);
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
									.stream().map(item ->
											new PaymentItem(
													item.getProduct().getBarcode(),
													item.getQuantity(),
													item.getProduct().getName(),
													item.getProduct().getMeasureUnit().getName(),
													item.getUnitPrice()
											))
									.collect(Collectors.toSet())
					);
					response.setData(responseData);
					response.setStatusCode(HttpStatus.CREATED.value());
					response.setMessage("Updated");

				}
			}
			return response;
		}catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			errors.add("Invalid request");
			response.setErrors(errors);
			return response;
		}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<ReceiptDetail> getReceiptDetail(String orderCode) {
		Response<ReceiptDetail> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage("Failed to get receipt detail");

		try{
			//check user
			Users user = getSessionUser(usersRepo);

			if(user == null){
				errors.add("Invalid session");
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
				response.setErrors(errors);
            }else {
				Receipts receipt = receiptsRepo.findByOrderCode(orderCode).orElse(null);

				if(receipt == null){
					response.setStatusCode(HttpStatus.NOT_FOUND.value());
					response.setMessage("Receipt not found");
                }else {
					response.setData(new ReceiptDetail(
							receipt.getOrderCode(),
							receipt.getCreator().getFullName(),
							receipt.getAmount(),
							receipt.getCustomerName(),
							receipt.getCustomerPhone(),
							receipt.isPaid(),
							receipt.getCreatedDate(),
							receipt.getPaymentDate(),
							receipt.getStore().getStoreName(),
							receipt.getStore().getAddress().toString(),
							receipt.getStore().getOwner().getPhone()
					));
					response.setMessage("Success");
					response.setStatusCode(HttpStatus.OK.value());

                }
            }
            return response;

        }catch (Exception e){
			System.out.println(e.getLocalizedMessage());
			errors.add("Invalid request");
			response.setErrors(errors);
			return response;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response<List<ReceiptInfo>> searchReceipts(ReceiptSearchCriteria request) {
		Response<List<ReceiptInfo>> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage("Nothing found");

		try{
			//check users and store
			Users user = getSessionUser(usersRepo);
			Stores store = getSessionStore(usersRepo, request.storeId());

			if(user == null || store == null){
				errors.add("Invalid session");
				response.setErrors(errors);
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            }else{
				//Steps
				// . Check for cashier phone or name
				// . Check for customer phone or name
				// . Check with start date and/or end date

				int pageNo = Math.max(request.pageNo(), 0);


				//get unfiltered data
				Sort ascSort = PageRequest.of(pageNo, pageSize, Sort.by("createdDate")).getSort().ascending();
				Sort descSort = PageRequest.of(pageNo, pageSize, Sort.by("createdDate")).getSort().descending();

				List<Receipts> receipts = receiptsRepo.findAll(request.sortDirection() == 1 ? ascSort : descSort);

				//filter name or phone
				if(!request.cashierNameOrPhone().isEmpty()){
					//search by cashier
					if(request.cashierNameOrPhone().matches(Regex.NAME)){
						//search by name
						receipts = receipts
								.stream()
								.filter(receipts1 ->
										receipts1.getCreator().getFullName()
												.contains(request.cashierNameOrPhone()))
								.collect(Collectors.toList());
					}else {
						receipts = receipts
								.stream()
								.filter(receipts1 ->
										receipts1.getCreator().getPhone()
												.contains(request.cashierNameOrPhone()))
								.collect(Collectors.toList());
					}
				}

				if (!request.customerNameOrPhone().isEmpty()){
					if(request.customerNameOrPhone().matches(Regex.NAME)){
						//search by name
						receipts = receipts
								.stream()
								.filter(receipts1 ->
										receipts1.getCustomerName()
												.contains(request.cashierNameOrPhone()))
								.collect(Collectors.toList());
					}else {
						receipts = receipts
								.stream()
								.filter(receipts1 ->
										receipts1.getCustomerPhone()
												.contains(request.customerNameOrPhone()))
								.collect(Collectors.toList());
					}

				}

				//filter date
				if(request.fromDate() == null && request.toDate() != null){
					receipts = receipts.stream()
							.filter(r ->
								r.getCreatedDate().isBefore(request.toDate())
							)
							.collect(Collectors.toList());
				}
				if (request.fromDate() != null  && request.toDate() == null){
					receipts = receipts.stream()
							.filter(r ->
									r.getCreatedDate().isAfter(request.fromDate())
							)
							.collect(Collectors.toList());
				}
				if (request.toDate() != null || request.fromDate() != null) {
					receipts = receipts.stream()
							.filter(r ->
									r.getCreatedDate().isBefore(request.toDate())
							)
							.filter(r ->
									r.getCreatedDate().isAfter(request.fromDate())
									)
							.collect(Collectors.toList());
				}

				//return data
				List<ReceiptInfo>  responseData = new ArrayList<>();

				for (Receipts r : receipts){
					responseData.add(
							new ReceiptInfo(
									r.getCustomerName(),
									r.getCustomerPhone(),
									r.getCreatedDate(),
									r.getCreator().getFullName(),
									r.isPaid(),
									r.getAmount()
							)
					);
				}

				response.setData(responseData);
				response.setMessage("Success");
				response.setStatusCode(HttpStatus.OK.value());
            }
            return response;

        }catch (Exception e){
			System.out.println(e.getLocalizedMessage());
			errors.add("Invalid request");
			response.setErrors(errors);

			return response;
		}
	}


}
