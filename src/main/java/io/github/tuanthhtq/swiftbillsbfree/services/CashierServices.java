package io.github.tuanthhtq.swiftbillsbfree.services;

import io.github.tuanthhtq.swiftbillsbfree.dtos.Response;
import io.github.tuanthhtq.swiftbillsbfree.dtos.payment.*;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ProductInfoResponse;
import io.github.tuanthhtq.swiftbillsbfree.dtos.product.ProductSearchCriteria;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @author io.github.tuanthhtq
 */

public interface CashierServices {


	/**
	 * Get product information by barcode or product id
	 *
	 * @param request {@link ProductSearchCriteria}
	 * @return {@link ProductInfoResponse}
	 */
	Response<ProductInfoResponse> getProductInfo(ProductSearchCriteria request);

	/**
	 * Create bill
	 *
	 * @param request       {@link PaymentRequest}
	 * @param bindingResult fields validation result
	 * @return {@link PaymentResponse}
	 */
	Response<PaymentResponse> createBill(PaymentRequest request, BindingResult bindingResult);

	/**
	 * Check that this bill is paid
	 *
	 * @param orderCode auto-generated order code
	 * @return {@link PaymentResponse}
	 */
	Response<PaymentResponse> confirmPayment(String orderCode);

	/**
	 * Get receipt detail
	 *
	 * @param orderCode auto-generated order code upon creation
	 * @return {@link ReceiptDetail}
	 */
	Response<ReceiptDetail> getReceiptDetail(String orderCode);

	/**
	 * Search for receipt base on criteria
	 *
	 * @param request       {@link ReceiptSearchCriteria}
	 * @param bindingResult fields validation result
	 * @return List of {@link ReceiptInfo}
	 */
	Response<List<ReceiptInfo>> searchReceipts(ReceiptSearchCriteria request, BindingResult bindingResult);
}
