package co.paylot.android;

import java.util.List;

import co.paylot.android.models.HashedTxData;
import co.paylot.android.models.Merchant;
import co.paylot.android.models.ProcessorItem;
import co.paylot.android.models.Transaction;
import co.paylot.android.models.TxData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public interface RestService {
    @POST("sign")
    Call<HashedTxData> signTransaction(@Body TxData data);

    @POST("process")
    Call<List<ProcessorItem>> processTransaction(@Body HashedTxData data);

    @POST("transactions")
    Call<Transaction> createTransaction(@Body ProcessorItem data);

    @PUT("transactions/{id}")
    Call<Transaction> updateTransaction(@Path("id")String id, @Body ProcessorItem data);

    @GET("transactions/{id}")
    Call<Transaction> getTransaction(@Path("id")String id);

    @GET("merchants/key/{key}")
    Call<Merchant> getMerchantByKey(@Path("key") String key);
}
