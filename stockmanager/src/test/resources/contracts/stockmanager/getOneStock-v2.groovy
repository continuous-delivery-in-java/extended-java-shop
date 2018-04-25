package contracts.stockmanager

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        headers {
            accept("application/vnd.stock.v2+json")
        }
        method 'GET'
        url '/stocks/123'
    }
    response {
        status 200
        body(
                productId: "123",
                sku: "sku-123",
                amountAvailable: [
                        total      : 10,
                        perPurchase: 5
                ]
        )
        headers {
            contentType('application/vnd.stock.v2+json')
        }
    }
}
