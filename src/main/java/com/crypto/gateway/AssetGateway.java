package com.crypto.gateway;

import com.crypto.usecase.CurrentPrice;

public interface AssetGateway {

    CurrentPrice getPriceBySymbol(String symbol);
}
