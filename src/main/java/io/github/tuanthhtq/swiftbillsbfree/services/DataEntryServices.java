package io.github.tuanthhtq.swiftbillsbfree.services;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ImportCreationRequest;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ImportCreationResponse;
import org.springframework.validation.BindingResult;

/**
 * @author io.github.tuanthhtq
 */

public interface DataEntryServices {

	/**
	 * Create import
	 * Steps:
	 * <ul>
	 *     <li>Check supplier, create new if not exists</li>
	 *     <li>Loop through products list and:
	 *      <ol>
	 *         <li>Check brand, create new if not exists</li>
	 *         <li>Check category, create new if not exists</li>
	 *         <li>Check measure unit, create new if not exists</li>
	 *         <li>Check bar code, combine if exist and create new if not exists</li>
	 *      </ol>
	 *     </li>
	 * </ul>
	 *
	 * @param request       {@link ImportCreationRequest}
	 * @param bindingResult fields validation result
	 * @return {@link  ImportCreationResponse}
	 */
	Response<ImportCreationResponse> importGoods(ImportCreationRequest request, BindingResult bindingResult);
}
