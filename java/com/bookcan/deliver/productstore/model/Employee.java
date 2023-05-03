package com.bookcan.deliver.productstore.model;

 
public class Employee {
    private String emp_id;
    private String emp_code;
    private String first_name;
    private String last_name;
    private String emp_mob_no;
    private String role_value;


    public Employee(String emp_id, String emp_code, String first_name, String last_name, String emp_mob_no, String role_value) {
        this.emp_id = emp_id;
        this.emp_code = emp_code;
        this.first_name = first_name;
        this.last_name = last_name;
        this.emp_mob_no = emp_mob_no;
        this.role_value = role_value;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmp_mob_no() {
        return emp_mob_no;
    }

    public String getRole_value() {
        return role_value;
    }
}
