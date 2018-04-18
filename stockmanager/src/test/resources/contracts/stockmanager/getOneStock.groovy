package contracts.stockmanager

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/stocks/123'
    }
    response {
        status 200
        body(
                productId: "123",
                sku: "sku-123",
                amountAvailable: 10
        )
        headers {
            contentType('application/json')
        }
    }
}
