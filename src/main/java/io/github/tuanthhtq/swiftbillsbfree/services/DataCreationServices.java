package io.github.tuanthhtq.swiftbillsbfree.services;

import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleIdNameDto;
import io.github.tuanthhtq.swiftbillsbfree.dtos.productDependent.SimpleSupplierDto;
import io.github.tuanthhtq.swiftbillsbfree.entities.Brands;
import io.github.tuanthhtq.swiftbillsbfree.entities.Categories;
import io.github.tuanthhtq.swiftbillsbfree.entities.MeasureUnit;
import io.github.tuanthhtq.swiftbillsbfree.entities.Suppliers;

/**
 * @author io.github.tuanthhtq
 */

public interface DataCreationServices {

	/**
	 * Return or create new supplier if not exist
	 *
	 * @param request {@link SimpleSupplierDto}
	 * @return {@link Suppliers} or null if supplier is already exists
	 */
	Suppliers getSupplier(SimpleSupplierDto request);


	/**
	 * Return or create new category if not exist
	 *
	 * @param request {@link SimpleSupplierDto}
	 * @return {@link Categories} or null if supplier is already exists
	 */
	Categories getProductCategory(SimpleIdNameDto request);

	/**
	 * Return or create new category if not exist
	 *
	 * @param request {@link SimpleSupplierDto}
	 * @return {@link Categories} or null if supplier is already exists
	 */
	Brands getProductBrand(SimpleIdNameDto request);

	/**
	 * Return or create new measure unit if not exist
	 *
	 * @param request {@link SimpleSupplierDto}
	 * @return {@link MeasureUnit} or null if supplier is already exists
	 */
	MeasureUnit getMeasureUnit(SimpleIdNameDto request);


}
