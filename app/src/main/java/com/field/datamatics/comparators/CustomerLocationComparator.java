package com.field.datamatics.comparators;

import com.field.datamatics.database.Customer;
import com.field.datamatics.database.JoinClientRoutePlan;

import java.util.Comparator;

/**
 * Created by Anoop on 26-11-2016.
 */
public class CustomerLocationComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer lhs, Customer rhs) {
        return lhs.Location.compareTo(rhs.Location);
    }
}
