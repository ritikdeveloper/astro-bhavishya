package com.astro.AstroRitaChaturvedi.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public String createOrder(double amount) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(amount * 100)); // convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + UUID.randomUUID().toString().substring(0, 8));
            orderRequest.put("payment_capture", 1); // auto capture

            Order order = razorpayClient.orders.create(orderRequest);
            return order.get("id"); // Correctly returns the order ID string
        } catch (RazorpayException | JSONException e) {
            // Log the exception properly in a real application
            e.printStackTrace();
            throw new RuntimeException("Razorpay order creation failed: " + e.getMessage());
        }
    }

    /**
     * Verifies the payment signature.
     *
     * @param orderId The Razorpay order ID.
     * @param paymentId The Razorpay payment ID.
     * @param razorpaySignature The signature received from Razorpay.
     * @return true if the signature is valid, false otherwise.
     */
    public boolean verifyPayment(String orderId, String paymentId, String razorpaySignature) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            // Utils.verifyPaymentSignature will perform the HMAC-SHA256 hashing internally
            // and compare it with the provided signature.
            // It throws a RazorpayException if verification fails (e.g., signature mismatch).
            // It is a void method; if it doesn't throw an exception, verification is successful.
            Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
            return true; // Signature is valid
        } catch (RazorpayException e) {
            // This exception is typically thrown for signature mismatch or other Razorpay errors.
            System.err.println("Razorpay signature verification failed: " + e.getMessage()); // Use a proper logger
            return false; // Signature is invalid
        } catch (JSONException e) {
            // This exception occurs if there's an issue constructing the JSONObject.
            System.err.println("Error creating JSON for Razorpay verification: " + e.getMessage()); // Use a proper logger
            return false;
        }
    }
}
