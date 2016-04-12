package com.prarthnasl.floatingicon.controllers;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prarthnasl.floatingicon.R;
import com.prarthnasl.floatingicon.controllers.baseControllers.BaseActivity;
import com.prarthnasl.floatingicon.helpers.ShareHelper;
import com.prarthnasl.floatingicon.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prarthnasl on 4/2/2016.
 */
public class SelectionWindowActivity extends BaseActivity {

    public static final String TAG = "SelectionWindowActivity";

    private GridLayoutManager gridLayout;
    private SparseBooleanArray selectedItems;

    private RecyclerView productRecyclerView;
    private RecyclerViewAdapter productAdapter;

    private TextView selectedItemsTextView;
    private TextView shareTextView;

    private ImageView whatsappIcon;

    private List<Product> productInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_window_layout);
        setFinishOnTouchOutside(false);

        initUI();
        setListeners();
    }

    private void initUI() {

        selectedItems = new SparseBooleanArray();

        selectedItemsTextView = (TextView) findViewById(R.id.selected_products_text);
        shareTextView = (TextView) findViewById(R.id.share_text);

        whatsappIcon = (ImageView) findViewById(R.id.whatsapp_icon);

        List<Product> productList = getAllProducts();
        gridLayout = new GridLayoutManager(SelectionWindowActivity.this, 2);

        productRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(gridLayout);
        productAdapter = new RecyclerViewAdapter(SelectionWindowActivity.this, productList);
        productRecyclerView.setAdapter(productAdapter);
    }

    private void setListeners() {

        whatsappIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedItemCount() > 0) {
                    ShareHelper shareHelper = new ShareHelper(SelectionWindowActivity.this);
                    shareHelper.shareImageWhatsApp(getSelectedItems());
                    finish();
                } else {
                    Toast.makeText(SelectionWindowActivity.this, getResources().getString(R.string.select_products_text), Toast.LENGTH_SHORT).show();
                }
            }
        });
        productRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        toggleSelection(view, position);
                        selectedItemsTextView.setText(String.format(getSelectedItemCount() + " " + "Product(s) Selected"));
                        shareTextView.setTextColor(getResources().getColor(getSelectedItemCount() > 0 ? R.color.dark_text_color : R.color.light_gray));

                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private List<Product> getAllProducts() {

        productInfo = new ArrayList<>();
        productInfo.add(new Product("Product 1", R.drawable.sample_product_one));
        productInfo.add(new Product("Product 2", R.drawable.sample_product_two));
        productInfo.add(new Product("Product 3", R.drawable.sample_product_three));
        productInfo.add(new Product("Product 4", R.drawable.sample_product_four));
        productInfo.add(new Product("Product 5", R.drawable.sample_product_five));
        productInfo.add(new Product("Product 6", R.drawable.sample_product_six));
        productInfo.add(new Product("Product 7", R.drawable.sample_product_seven));
        productInfo.add(new Product("Product 8", R.drawable.sample_product_eight));

        return productInfo;
    }

    public void toggleSelection(View view, int position) {
        productAdapter.notifyItemChanged(position);
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);

            view.findViewById(R.id.view_overlay).setVisibility(View.GONE);
            view.findViewById(R.id.view_overlay_check).setVisibility(View.GONE);
        } else {
            selectedItems.put(position, true);

            view.findViewById(R.id.view_overlay).setVisibility(View.VISIBLE);
            view.findViewById(R.id.view_overlay_check).setVisibility(View.VISIBLE);
        }
        productAdapter.notifyItemChanged(position);
    }

    public void clearSelections() {
        selectedItems.clear();
        productAdapter.notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Product> getSelectedItems() {
        List<Product> selectedProducts = new ArrayList<>();
        for (int index = 0; index < selectedItems.size(); index++) {
            selectedProducts.add(productInfo.get(selectedItems.keyAt(index)));
        }
        return selectedProducts;
    }
}
