package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ImportCreationProduct;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ImportCreationRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ImportCreationResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleIdNameDto;
import io.github.tuanthhtq.swiftbillsbfree.entities.*;
import io.github.tuanthhtq.swiftbillsbfree.repositories.ProductsRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.SuppliersRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.UsersRepository;
import io.github.tuanthhtq.swiftbillsbfree.services.DataCreationServices;
import io.github.tuanthhtq.swiftbillsbfree.services.DataEntryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author io.github.tuanthhtq
 */

@Service
public class DataEntryServicesImpl extends CommonConcrete implements DataEntryServices {

	private final UsersRepository usersRepo;
	private final SuppliersRepository suppliersRepo;
	private final ProductsRepository productsRepo;
	private final DataCreationServices dataCreationServices;

	@Autowired
	public DataEntryServicesImpl(
			UsersRepository usersRepo,
			SuppliersRepository suppliersRepository,
			ProductsRepository productsRepo,
			DataCreationServices dataCreationServices

	) {
		this.usersRepo = usersRepo;
		this.suppliersRepo = suppliersRepository;
		this.productsRepo = productsRepo;
		this.dataCreationServices = dataCreationServices;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Response<ImportCreationResponse> importGoods(ImportCreationRequest request, BindingResult bindingResult) {
		Response<ImportCreationResponse> response = new Response<>();
		Set<String> errors = new HashSet<>();

		response.setMessage("Failed to import data");

		float estCost = 0;
		float estIncome = 0;

		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(error.getDefaultMessage());
			}
			response.setErrors(errors);
		} else {
			//check session
			Users user = getSessionUser(usersRepo);

			if (user == null) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
				errors.add("Invalid session");
				response.setErrors(errors);
			} else {

				//get supplier
				Suppliers supplier = suppliersRepo.findById(request.supplier().id()).orElse(null);
				if (supplier == null) {  //supplier == null mean not exists
					supplier = dataCreationServices.getSupplier(request.supplier());
				}

				//iterate through products to extract brands, categories, measure units
				Set<SimpleIdNameDto> brandNames = request.products().stream()
						.map(p -> new SimpleIdNameDto(
								p.brand().id(), p.brand().name()
						))
						.collect(Collectors.toSet());

				Set<SimpleIdNameDto> categoryNames = request.products().stream()
						.map(p -> new SimpleIdNameDto(
								p.category().id(), p.category().name()
						))
						.collect(Collectors.toSet());

				Set<SimpleIdNameDto> unitNames = request.products().stream()
						.map(p -> new SimpleIdNameDto(
								p.measureUnit().id(), p.measureUnit().name()
						))
						.collect(Collectors.toSet());


				//get existing or create new
				Set<Brands> brands = brandNames.stream()
						.map(dataCreationServices::getProductBrand)
						.collect(Collectors.toSet());

				Set<Categories> categories = categoryNames.stream()
						.map(dataCreationServices::getProductCategory)
						.collect(Collectors.toSet());

				Set<MeasureUnit> units = unitNames.stream()
						.map(dataCreationServices::getMeasureUnit)
						.collect(Collectors.toSet());

				Set<Products> productsToBeSaved = new HashSet<>();
				//iterate through products
				for (ImportCreationProduct p : request.products()) {
					//get brand
					Brands b = brands.stream()
							.filter(i -> i.getName().equals(p.brand().name()))
							.findFirst()
							.orElse(null);
					//get category
					Categories c = categories.stream()
							.filter(i -> i.getName().equals(p.brand().name()))
							.findFirst()
							.orElse(null);
					//get measure unit
					MeasureUnit m = units.stream()
							.filter(i -> i.getName().equals(p.brand().name()))
							.findFirst()
							.orElse(null);

					//calculate estimated cost and income
					estCost += p.costPrice() * p.amount();
					estIncome += (p.price() - p.costPrice()) * p.amount();

					//check if product exists
					Products product = productsRepo.findByBarcode(p.barcode()).orElse(null);

					if (product == null) {
						//create new product
						product = new Products(
								p.productName(),
								p.productDescription(),
								p.price(),
								p.costPrice(),
								p.amount(),
								m,
								p.barcode(),
								c,
								supplier,
								p.imageUrls().stream().map(Images::new).collect(Collectors.toSet()),
								b
						);
					} else {
						product.setStock(product.getStock() + p.amount());
					}
					productsToBeSaved.add(product);
				}
				//persist changes
				productsRepo.saveAll(productsToBeSaved);

				//response data
				ImportCreationResponse responseData = new ImportCreationResponse(
						user.getFullName(),
						estCost,
						estIncome
				);

				response.setData(responseData);
				response.setStatusCode(HttpStatus.CREATED.value());
				response.setMessage("Successfully imported data");
			}
		}
		return response;
	}
}
