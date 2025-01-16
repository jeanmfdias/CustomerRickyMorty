package com.rickymorty.customer.contracts;

public interface ITranslate {
    <T> T getData(String json, Class<T> tClass);
}
