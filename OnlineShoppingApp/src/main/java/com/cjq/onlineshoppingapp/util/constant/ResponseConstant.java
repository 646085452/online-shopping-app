package com.cjq.onlineshoppingapp.util.constant;

public class ResponseConstant {

    public static final String VALIDATION_FAIL = "Validation failed: ";

    // user
    public static final String USER_NOT_FOUND = "User Not Found";
    public static final String PRODUCT_ON_WATCHLIST = "Product is already on the watchlist";
    public static final String PRODUCT_NOT_ON_WATCHLIST = "Product is not on the watchlist";
    public static final String WATCHLIST_ADD_SUCCESSFUL = "Product added to watchlist";
    public static final String WATCHLIST_REMOVE_SUCCESSFUL = "Product removed from Watchlist";
    public static final String EMPTY_WATCHLIST = "No products in watchlist or products are out of stock";
    public static final String WATCHLIST_RETRIEVE_SUCCESSFUL = "Product retrieved from Watchlist successfully";

    // register and login
    public static final String USERNAME_OR_EMAIL_IN_USE = "Username or email address already in use";
    public static final String USER_REGISTER_SUCCESSFUL = "User registered successfully";
    public static final String INCORRECT_CREDENTIALS = "Incorrect credentials, please try again";
    public static final String USER_LOGIN_SUCCESSFUL = "Login successful";

    // product
    public static final String PRODUCT_NOT_FOUND = "Product not found with ID: ";
    public static final String PRODUCT_RETRIEVED_SUCCESSFUL = "Product details retrieved successfully";
    public static final String PRODUCT_CREATE_SUCCESSFUL = "Product created successfully";
    public static final String PRODUCT_UPDATE_SUCCESSFUL = "Product updated successfully";

    // order
    public static final String ORDER_PLACE_SUCCESSFUL = "Order placed successfully";
    public static final String ORDER_NOT_FOUND = "Order not found with ID: ";

    public static final String CANNOT_CANCEL_COMPLETED_ORDER = "Completed orders cannot be canceled";
    public static final String CANNOT_CANCEL_OTHER_ORDER = "Only orders with status 'Processing' can be canceled";
    public static final String CANNOT_COMPLETE_CANCALED_ORDER = "Canceled orders cannot be completed";
    public static final String CANNOT_COMPLETE_OTHER_ORDER = "Only orders with status 'Processing' can be completed";

    public static final String ORDER_CANCEL_SUCCESSFUL = "Order has been successfully canceled";
    public static final String ORDER_COMPLETE_SUCCESSFUL = "Order has been successfully completed";

    public static final String NO_AUTHORITY = "You do not have the required authority to access this resource";
    public static final String USER_ONLY = "This resource is only applicable to buyers. Please do not access it if you are the seller.";


}
