package com.example.android.justjava;

import java.text.NumberFormat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 */

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int quantity;
    private TextView mQuantityTextView;
    private TextView mOrderSummaryTextView;
    private CheckBox mWhippedCreamCheck;
    private CheckBox mChocolateCheck;
    private EditText mName;
    private String TAG_1 = "You cannot order more than 100 cups of coffee!";
    private String TAG_2 = "You cannot order less than one cup of coffee!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        mWhippedCreamCheck = (CheckBox) findViewById(R.id.whipped_cream_check);
        mChocolateCheck = (CheckBox) findViewById(R.id.chocolate_check);
        mName = (EditText) findViewById(R.id.name);
        quantity = Integer.parseInt(mQuantityTextView.getText().toString());
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        quantity++;
        if (quantity > 100) {
            Toast.makeText(MainActivity.this, TAG_1, Toast.LENGTH_SHORT).show();
            quantity = 100;
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        quantity--;
        if (quantity < 1) {
            Toast.makeText(MainActivity.this, TAG_2, Toast.LENGTH_SHORT).show();
            quantity = 1;
        }
        else {
            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        boolean isWhipped = mWhippedCreamCheck.isChecked();
        boolean isChocolate = mChocolateCheck.isChecked();
        String name = mName.getText().toString();
        int price = calculatePrice(isWhipped, isChocolate);
        String order = createOrderSummary(name, price, isWhipped, isChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for "+ name);
        intent.putExtra(Intent.EXTRA_TEXT, order);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order. Adds a $1 and $2 charge for whipped cream and chocolate, respectively.
     *
     * @param hasWhippedCream is whether or not user selected whipped cream
     * @param hasChocolate is whether or not user selected chocolate
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;

        // First, check if user has selected whipped cream
        if (hasWhippedCream) {
            basePrice += 1;
        }

        // Then, check if user has selected chocolate
        if (hasChocolate) {
            basePrice += 2;
        }

        return quantity * basePrice;
    }

    /**
     * Provides the order summary of a user
     *
     * @param nameOfUser
     * @param priceOfCoffee of the cups of coffee ordered
     * @param hasWhippedCream is whether whipped cream is selected as a topping
     * @param hasChocolate is whether or not a user wants a chocolate topping
     * @return order summary
     */
    private String createOrderSummary(String nameOfUser, int priceOfCoffee, boolean hasWhippedCream, boolean hasChocolate) {
        String orderSummary =
                getString(R.string.order_summary_name, nameOfUser) + "\n" +
                getString(R.string.order_summary_whipped_cream, hasWhippedCream) + "\n" +
                getString(R.string.order_summary_chocolate, hasChocolate) + "\n" +
                getString(R.string.order_summary_quantity, quantity) + "\n" +
                getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(priceOfCoffee)) + "\n" +
                getString(R.string.thank_you);

        return orderSummary;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        mQuantityTextView.setText("" + number);
    }
}