package contracts.stockmanager

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
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
                                amountAvailable: 10
                        ],
                        [
                                productId      : "2",
                                sku            : "sku-2",
                                amountAvailable: 20
                        ],
                        [
                                productId      : "3",
                                sku            : "sku-3",
                                amountAvailable: 30
                        ]
                ]
        )
        headers {
            contentType('application/json')
        }
    }
}
