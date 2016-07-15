package com.sahaware.resysbni.repository;

import java.util.List;

/**
 * Created by DILo on 5/14/2016.
 */
public interface IAsyncRepository {
    List<Boolean> getUserDetail(String username, String password);
}
