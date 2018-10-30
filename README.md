[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=plastic)](https://android-arsenal.com/api?level=16)

# Paylot Android SDK
This is the official Android library of easy integration of Paylot into any android app.

## Adding to Your Project
To install the SDK, follow the steps below.

For Maven,

```xml
<dependency>
  <groupId>co.paylot.android</groupId>
  <artifactId>paylot</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

For Gradle,

```sh
dependencies {
    implementation 'co.paylot.android:paylot:1.0.3'
}
```

## Usage

### Create a PayotPay Instance
Firstly, to accept payments, you need to create and initialize an instance of the PaylotPay class. This launches the Paylot SDK payments page. The PaylotPay instance accepts an **activity** as it's only parameter.

```java
PaylotPay paylotPay = new PaylotPay(this)
                  .setAmount(amount)
                  .setCurrency("NGN")
                  .setEmail(email)
                  .setShouldSendMail(true)
                  .setPublicKey(BuildConfig.PaylotPublicKey)
                  .setSecretKey(BuildConfig.PaylotSecretKey)
                  .setSubAccount(DEFAULT_SUBACCOUNT)
                  .setReference(String.valueOf(new Date().getTime()));

paylotPay.initialize();
```
The following describes the parameters required to make a payment.

| function        | parameter           | type | required  |
| ------------- |:-------------:| -----:| -----:|
| setAmount(amount)      |  This is the amount to be charged | `double` | Required
| setCurrency(currency) | This is the currency the amount is specified in. (NGN only allowed right now.) | `String` | Required
| setEmail(email) | This is the email address of the customer | `String` | Required
| setPublicKey(publicKey) | Merchant's public key | `String` | Required
| setSecretKey(secretKey) | Merchant's secret key | `String` | Required
| setShouldSendMail(sendMail) | This specifies if the customer should receive a receipt.  | `String` | Optional
| setReference(reference) | This is the unique reference for the specific payment. If absent, an automatic reference is generated | `String` | Optional
| setSubAccount(subaccount) | This is reference for the subaccount (in split payment scenario) | `String` | Optional


###  2. Handle the response
In the calling activity, override the `onActivityResult` method to receive the payment response as shown below

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PaylotConstants.PAY_REQUEST_CODE && resultCode == RESULT_OK){
            //This gets the transaction details.
            Transaction transaction = data.getParcelableExtra("transaction");
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
```
The intent's `transaction` object contains the Transaction object from the Paylot API. 

Rest of documentation coming soon...
