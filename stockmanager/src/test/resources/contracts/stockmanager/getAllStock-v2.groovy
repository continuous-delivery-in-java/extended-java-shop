package contracts.stockmanager

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        headers {
            accept("application/vnd.stock.v2+json")
        }
        method 'GET'
        url '/stocks'
    }
    response {
        status 200
        body(
                [
                        [
                                productId      : "1",
                                sku            : "sku-1",
                                amountAvailable: [
                                        total      : 10,
                                        perPurchase: 5
                                ]
                        ],
                        [
                                productId      : "2",
                                sku            : "sku-2",
                                amountAvailable: [
                                        total      : 20,
                                        perPurchase: 10
                                ]
                        ],
                        [
                                productId      : "3",
                                sku            : "sku-3",
                                amountAvailable: [
                                        total      : 30,
                                        perPurchase: 15
                                ]
                        ]
                ]
        )
        headers {
            contentType('application/vnd.stock.v2+json')
        }
    }
}
