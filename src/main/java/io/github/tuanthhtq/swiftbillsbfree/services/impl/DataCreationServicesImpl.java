package io.github.tuanthhtq.swiftbillsbfree.services.impl;

import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleIdNameDto;
import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleSupplierDto;
import io.github.tuanthhtq.swiftbillsbfree.entities.Brands;
import io.github.tuanthhtq.swiftbillsbfree.entities.Categories;
import io.github.tuanthhtq.swiftbillsbfree.entities.MeasureUnit;
import io.github.tuanthhtq.swiftbillsbfree.entities.Suppliers;
import io.github.tuanthhtq.swiftbillsbfree.repositories.BrandsRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.CategoriesRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.MeasureUnitRepository;
import io.github.tuanthhtq.swiftbillsbfree.repositories.SuppliersRepository;
import io.github.tuanthhtq.swiftbillsbfree.services.DataCreationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author io.github.tuanthhtq
 */

@Service
public class DataCreationServicesImpl implements DataCreationServices {
	private final SuppliersRepository suppliersRepo;
	private final CategoriesRepository categoriesRepo;
	private final BrandsRepository brandsRepo;
	private final MeasureUnitRepository measureUnitRepo;

	@Autowired
	public DataCreationServicesImpl(
			SuppliersRepository suppliersRepo,
			CategoriesRepository categoriesRepo,
			BrandsRepository brandsRepo,
			MeasureUnitRepository measureUnitRepo
	) {
		this.suppliersRepo = suppliersRepo;
		this.categoriesRepo = categoriesRepo;
		this.brandsRepo = brandsRepo;
		this.measureUnitRepo = measureUnitRepo;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Suppliers getSupplier(SimpleSupplierDto request) {
		try {
			Suppliers supplier = new Suppliers(
					request.name(),
					request.address(),
					request.phone()
			);
			return suppliersRepo.save(supplier);
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public Categories getProductCategory(SimpleIdNameDto request) {
		try {
			return categoriesRepo.findByName(request.name())
					.orElseGet(() -> categoriesRepo.save(new Categories(request.name())));
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Brands getProductBrand(SimpleIdNameDto request) {
		try {
			return brandsRepo.findByName(request.name())
					.orElseGet(() -> brandsRepo.save(new Brands(request.name())));
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MeasureUnit getMeasureUnit(SimpleIdNameDto request) {
		try {
			return measureUnitRepo.findByName(request.name())
					.orElseGet(() -> measureUnitRepo.save(new MeasureUnit(request.name())));
		} catch (Exception ex) {
			System.err.println(ex.getLocalizedMessage());
			return null;
		}
	}
}
