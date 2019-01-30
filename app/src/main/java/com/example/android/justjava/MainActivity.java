/**
 * IMPORTANT: Make sure you are using the correct package name. 
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private int numOfCoffees=0;
    private final int priceOfOneCupOfCoffee = 5;
    private final int priceOfWhippedCreamPerCup = 2;
    private final int priceOfChocolatePerCup = 1;
    private boolean whippedCream = false;
    private boolean chocolateTopping = false;
    private String nameStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        this.nameStr = getName();
        displayQuantity(this.numOfCoffees);
        this.getToppings();
        int price = calculatePrice();
        String orderSummary = createOrderSummary(price);
        //displayOrderSummary(orderSummary);
        emailOrderSummary(orderSummary);
    }

    /**
     * This method is called when the - button is pressed.
     */
    public void decrementQuantity(View view) {
        if (this.numOfCoffees<=1) {
            Toast.makeText(this, getString(R.string.toast_qty_too_small), Toast.LENGTH_SHORT).show();
            return;
        }
        this.numOfCoffees--;
        displayQuantity(this.numOfCoffees);
    }

    /**
     * This method is called when the + button is pressed.
     */
    public void incrementQuantity(View view) {
        if (this.numOfCoffees>=10) {
            Toast.makeText(this, getString(R.string.toast_qty_too_big), Toast.LENGTH_SHORT).show();
            return;
        }
        this.numOfCoffees++;
        displayQuantity(this.numOfCoffees);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method gets the toppings from the checkboxes.
     */
    private void getToppings() {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_check_box);
        this.whippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_check_box);
        this.chocolateTopping = chocolateCheckBox.isChecked();
    }

    /**
     * This method gets the name of customer from the edit text.
     */
    private String getName() {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        return nameEditText.getText().toString();
    }

    /**
     * This method displays the order summary on the screen.
     */
    private void displayOrderSummary(String orderSummary) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(orderSummary);
    }

    /**
     * This method emails the order summary.
     */
    private void emailOrderSummary(String orderSummary) {
        displayOrderSummary(orderSummary);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)+getString(R.string.email_subj_snippet)+this.nameStr);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice() {
        return this.numOfCoffees *
                (this.priceOfOneCupOfCoffee +
                        (this.whippedCream?this.priceOfWhippedCreamPerCup:0) +
                        (this.chocolateTopping?this.priceOfChocolatePerCup:0));
    }

    /**
     * This method creates an order summary to be displayed on screen
     *
     * @param price this is the price that was calculated
     *
     * @return String Order Summary String
     */
    private String createOrderSummary(int price) {
        String ordersummary = "";
        ordersummary += getString(R.string.order_status_name_label) + getString(R.string.order_status_label_value_delimiter) + this.nameStr + "\n";
        ordersummary += getString(R.string.order_status_quantity_label) + getString(R.string.order_status_label_value_delimiter)  + this.numOfCoffees + "\n";
        if (this.whippedCream) {
            ordersummary += getString(R.string.order_status_whipped_cream) + "\n";
        }
        if (this.chocolateTopping) {
            ordersummary += getString(R.string.order_status_chocolate) + "\n";
        }
        ordersummary += getString(R.string.order_status_total) + getString(R.string.order_status_label_value_delimiter) + NumberFormat.getCurrencyInstance().format(price) + "\n";
        ordersummary += getString(R.string.order_status_thank_you);
        return ordersummary;
    }
}