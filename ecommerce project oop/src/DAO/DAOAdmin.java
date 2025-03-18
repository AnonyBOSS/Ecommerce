package DAO;

import entities.Admin;
import entities.Database;

public class DAOAdmin implements CRUD<Admin> {

    @Override
    public void create(Admin admin) {
        Database.admins.add(admin);
    }

    @Override
    public Admin read(Admin admin) {
        for (Admin a : Database.admins) {
            if (a.getUserName().equals(admin.getUserName())) {
                System.out.println("Admin found: " + a);
                return admin;
            }
        }
        System.out.println( "Admin with username " + admin.getUserName() + " not found.");
        throw new IllegalArgumentException("Admin not found.");
    }

    @Override
    public void update(Admin admin) {
        for (Admin a : Database.admins) {
            if (a.getUserName().equals(admin.getUserName())) {
                try {
                    a.setPassword(admin.getPassword());
                    a.setDateOfBirth(admin.getDateOfBirth());
                    a.setRole(admin.getRole());
                    a.setWorkingHours(admin.getWorkingHours());
                    System.out.println("Admin details updated: " + a);
                }catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        System.out.println("Admin not found for update.");
    }

    @Override
    public void delete(Admin admin) {
        if (Database.admins.removeIf(a -> a.getUserName().equals(admin.getUserName()))) {
            System.out.println("Admin deleted: " + admin);
        } else {
            System.out.println( "Admin with username " + admin.getUserName() + " not found.");
        }
    }
}
