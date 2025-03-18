package Design;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Service.UserManager;
import entities.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ECommerceGUI extends Application {
    private Stage primaryStage;
    private Scene loginScene;
    private Scene registerScene;
    private Scene adminDashboard;
    private Scene customerDashboard;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("E-Commerce System");

        createLoginScene();
        createRegistrationScene();

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createLoginScene() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("E-Commerce System Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register New Account");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            try {
                currentUser = UserManager.loginUser(usernameField.getText(), passwordField.getText());
                if (currentUser instanceof Admin) {
                    createAdminDashboard((Admin) currentUser);
                    primaryStage.setScene(adminDashboard);
                } else if (currentUser instanceof Customer) {
                    createCustomerDashboard((Customer) currentUser);
                    primaryStage.setScene(customerDashboard);
                }
            } catch (Exception ex) {
                messageLabel.setText("Invalid username or password");
            }
        });

        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));

        loginLayout.getChildren().addAll(
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton,
                registerButton,
                messageLabel
        );

        loginScene = new Scene(loginLayout, 800, 600);
    }

    private void createRegistrationScene() {
        VBox registrationLayout = new VBox(10);
        registrationLayout.setPadding(new Insets(20));
        registrationLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Register New Account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Customer", "Admin");
        userTypeCombo.setValue("Select");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth (YYYY-MM-DD)");

        VBox customerFields = new VBox(10);
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        ComboBox<Customer.Gender> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Customer.Gender.values());
        TextField interestsField = new TextField();
        interestsField.setPromptText("Interests (comma-separated)");

        VBox adminFields = new VBox(10);
        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        TextField workingHoursField = new TextField();
        workingHoursField.setPromptText("Working Hours");

        userTypeCombo.setOnAction(e -> {
            if (userTypeCombo.getValue().equals("Customer")) {
                registrationLayout.getChildren().remove(adminFields);
                if (!registrationLayout.getChildren().contains(customerFields)) {
                    registrationLayout.getChildren().add(customerFields);
                }
            } else {
                registrationLayout.getChildren().remove(customerFields);
                if (!registrationLayout.getChildren().contains(adminFields)) {
                    registrationLayout.getChildren().add(adminFields);
                }
            }
        });

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        registerButton.setOnAction(e -> {
            try {
                if(userTypeCombo.getValue().equals("Select")){
                    throw new Exception("Please select user type");
                }
                else if (userTypeCombo.getValue().equals("Customer")) {
                    Customer customer = new Customer(
                            usernameField.getText(),
                            passwordField.getText(),
                            dobField.getText(),
                            addressField.getText(),
                            genderCombo.getValue(),
                            Arrays.asList(interestsField.getText().split(","))
                    );
                    UserManager.registerUser(customer);
                } else {
                    Admin admin = new Admin(
                            usernameField.getText(),
                            passwordField.getText(),
                            dobField.getText(),
                            roleField.getText(),
                            Integer.parseInt(workingHoursField.getText())
                    );
                    UserManager.registerUser(admin);
                }
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Registration successful!");
                primaryStage.setScene(loginScene);
            } catch (Exception ex) {
                messageLabel.setText("Registration failed: " + ex.getMessage());
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(loginScene));

        customerFields.getChildren().addAll(
                new Label("Address:"),
                addressField,
                new Label("Gender:"),
                genderCombo,
                new Label("Interests:"),
                interestsField
        );

        adminFields.getChildren().addAll(
                new Label("Role:"),
                roleField,
                new Label("Working Hours:"),
                workingHoursField
        );

        registrationLayout.getChildren().addAll(
                titleLabel,
                new Label("User Type:"),
                userTypeCombo,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Date of Birth:"),
                dobField,
                registerButton,
                backButton,
                messageLabel
        );
        registerScene =new Scene(registrationLayout, 800,600);
    }

    private void createAdminDashboard(Admin admin) {
        TabPane tabPane = new TabPane();

        Tab productsTab = new Tab("Products");
        productsTab.setClosable(false);
        VBox productsLayout = new VBox(10);
        productsLayout.setPadding(new Insets(10));

        TableView<Product> productTable = new TableView<>();
        addSearchFunctionality(productTable, productsLayout);
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getName()));

        productTable.getColumns().addAll(idCol, nameCol, descCol, priceCol, quantityCol, categoryCol);
        productTable.setItems(FXCollections.observableArrayList(Database.products));

        Button addProductButton = new Button("Add Product");
        Button editProductButton = new Button("Edit Product");
        Button deleteProductButton = new Button("Delete Product");

        HBox productButtons = new HBox(10);
        productButtons.getChildren().addAll(addProductButton, editProductButton, deleteProductButton);

        productsLayout.getChildren().addAll(productTable, productButtons);
        productsTab.setContent(productsLayout);

        Tab ordersTab = new Tab("Orders");
        ordersTab.setClosable(false);
        VBox ordersLayout = new VBox(10);
        ordersLayout.setPadding(new Insets(10));

        TableView<Order> orderTable = new TableView<>();
        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCustomer().getUserName()));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.getColumns().addAll(orderIdCol, customerCol, totalCol, statusCol);
        orderTable.setItems(FXCollections.observableArrayList(Database.orders));

        Button viewOrderButton = new Button("View Order Details");
        Button updateStatusButton = new Button("Update Status");

        HBox orderButtons = new HBox(10);
        orderButtons.getChildren().addAll(viewOrderButton, updateStatusButton);

        ordersLayout.getChildren().addAll(orderTable, orderButtons);
        ordersTab.setContent(ordersLayout);

        Tab categoriesTab = new Tab("Categories");
        categoriesTab.setClosable(false);
        VBox categoriesLayout = new VBox(10);
        categoriesLayout.setPadding(new Insets(10));

        TableView<Category> categoryTable = new TableView<>();
        TableColumn<Category, Integer> categoryIdCol = new TableColumn<>("ID");
        TableColumn<Category, String> categoryNameCol = new TableColumn<>("Name");

        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        categoryTable.getColumns().addAll(categoryIdCol, categoryNameCol);
        categoryTable.setItems(FXCollections.observableArrayList(Database.categories));

        Button addCategoryButton = new Button("Add Category");
        Button editCategoryButton = new Button("Edit Category");
        Button deleteCategoryButton = new Button("Delete Category");

        HBox categoryButtons = new HBox(10);
        categoryButtons.getChildren().addAll(addCategoryButton, editCategoryButton, deleteCategoryButton);

        categoriesLayout.getChildren().addAll(categoryTable, categoryButtons);
        categoriesTab.setContent(categoriesLayout);

        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username: " + admin.getUserName());
        Label roleLabel = new Label("Role: " + admin.getRole());
        Label workingHoursLabel = new Label("Working Hours: " + admin.getWorkingHours());
        Label dobLabel = new Label("Date of Birth: " + admin.getDateOfBirth());

        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setOnAction(e -> showEditAdminProfileDialog(admin, roleLabel, workingHoursLabel));
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            primaryStage.setScene(loginScene);
        });
        profileLayout.getChildren().addAll(
                usernameLabel,
                roleLabel,
                workingHoursLabel,
                dobLabel,
                editProfileButton,
                logoutButton
        );
        profileTab.setContent(profileLayout);
        tabPane.getTabs().addAll(productsTab, ordersTab, categoriesTab,createCustomersTab(),profileTab);
        addProductButton.setOnAction(e -> {
            showAddProductDialog();
            productTable.setItems(FXCollections.observableArrayList(Database.products));
        });

        editProductButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                showEditProductDialog(selectedProduct);
                productTable.setItems(FXCollections.observableArrayList(Database.products));
                productTable.refresh();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a product to edit");
            }
        });

        deleteProductButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                Admin.deleteProduct(selectedProduct);
                productTable.setItems(FXCollections.observableArrayList(Database.products));
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a product to delete");
            }
        });

        addCategoryButton.setOnAction(e -> {
            showAddCategoryDialog();
            categoryTable.setItems(FXCollections.observableArrayList(Database.categories));
        });

        editCategoryButton.setOnAction(e -> {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                showEditCategoryDialog(selectedCategory);
                categoryTable.setItems(FXCollections.observableArrayList(Database.categories));
                categoryTable.refresh();
                productTable.refresh();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a category to edit");
            }
        });

        deleteCategoryButton.setOnAction(e -> {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                Admin.deleteCategory(selectedCategory);
                categoryTable.setItems(FXCollections.observableArrayList(Database.categories));
                productTable.setItems(FXCollections.observableArrayList(Database.products));
                productTable.refresh();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a category to delete");
            }
        });

        updateStatusButton.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showUpdateOrderStatusDialog(selectedOrder);
                orderTable.refresh();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select an order to update");
            }
        });

        viewOrderButton.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showOrderDetailsDialog(selectedOrder);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select an order to view");
            }
        });
        adminDashboard = new Scene(tabPane, 800, 600);
    }

    private void createCustomerDashboard(Customer customer) {
        TabPane tabPane = new TabPane();

        Tab productsTab = new Tab("Products");
        productsTab.setClosable(false);
        VBox productsLayout = new VBox(10);
        productsLayout.setPadding(new Insets(10));

        TableView<Product> productTable = new TableView<>();
        addSearchFunctionality(productTable, productsLayout);
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategory().getName()));

        productTable.getColumns().addAll(nameCol, descCol, priceCol, categoryCol);
        productTable.setItems(FXCollections.observableArrayList(Database.products));

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, 1);
        Button addToCartButton = new Button("Add to Cart");
        TableView<Cart.CartItem> cartTable = new TableView<>();
        TableColumn<Cart.CartItem,String> cartNameCol = new TableColumn<>("Name");
        TableColumn<Cart.CartItem, Double> cartPriceCol = new TableColumn<>("Price");
        TableColumn<Cart.CartItem, Integer> cartQuantityCol = new TableColumn<>("Quantity");

        cartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        cartTable.getColumns().addAll(cartNameCol, cartPriceCol, cartQuantityCol);

        Label totalLabel = new Label("Total: $0.00");
        Button checkoutButton = new Button("Checkout");
        Button removeFromCartButton = new Button("Remove Selected");
        ComboBox<Order.PaymentMethod> paymentMethodComboBox = new ComboBox<>();
        paymentMethodComboBox.getItems().addAll(Order.PaymentMethod.values());
        paymentMethodComboBox.setValue(Order.PaymentMethod.CREDIT_CARD);

        updateCartDisplay(cartTable, customer, totalLabel);

        addToCartButton.setOnAction(e -> {
            try {
                Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    customer.addProductToCart(selectedProduct, quantitySpinner.getValue());
                    updateCartDisplay(cartTable, customer, totalLabel);
                }
            }catch(Exception exception){
                showAlert(Alert.AlertType.WARNING, "Warning", exception.getMessage());
            }
        });


        HBox productControls = new HBox(10);
        productControls.getChildren().addAll(new Label("Quantity:"), quantitySpinner, addToCartButton);
        productsLayout.getChildren().addAll(productTable, productControls);
        productsTab.setContent(productsLayout);

        Tab cartTab = new Tab("Cart");
        cartTab.setClosable(false);
        VBox cartLayout = new VBox(10);
        cartLayout.setPadding(new Insets(10));

        TableView<Order> orderTable = new TableView<>();
        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");

        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        removeFromCartButton.setOnAction(e -> {
            Cart.CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                customer.removeProductFromCart(selectedItem.getProduct());
                updateCartDisplay(cartTable, customer, totalLabel);
            }
        });

        HBox cartButtons = new HBox(10);
        cartButtons.getChildren().addAll(removeFromCartButton, checkoutButton,paymentMethodComboBox);
        cartLayout.getChildren().addAll(cartTable, totalLabel, cartButtons);
        cartTab.setContent(cartLayout);

        Tab ordersTab = new Tab("Orders");
        ordersTab.setClosable(false);
        VBox ordersLayout = new VBox(10);
        ordersLayout.setPadding(new Insets(10));

        orderTable.getColumns().addAll(orderIdCol, totalCol, statusCol, dateCol);
        orderTable.setItems(FXCollections.observableArrayList(customer.getOrders()));

        Button viewOrderDetailsButton = new Button("View Details");
        viewOrderDetailsButton.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showOrderDetailsDialog(selectedOrder);
            }
        });

        ordersLayout.getChildren().addAll(orderTable, viewOrderDetailsButton);
        ordersTab.setContent(ordersLayout);

        Tab profileTab = new Tab("Profile");
        profileTab.setClosable(false);
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username: " + customer.getUserName());
        Label balanceLabel = new Label("Balance: $" + customer.getBalance());
        Label addressLabel = new Label("Address: " + customer.getAddress());
        Label genderLabel = new Label("Gender: " + customer.getGender());
        Label interestsLabel = new Label("Interests: " + String.join(", ", customer.getInterests()));

        checkoutButton.setOnAction(e -> {
            Order.PaymentMethod selectedMethod = paymentMethodComboBox.getValue();
            if (selectedMethod == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a payment method.");
                return;
            }
            try {
                Order order = customer.checkout(selectedMethod);
                updateOrderTable(orderTable, customer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order placed successfully!");
                cartTable.setItems(FXCollections.observableArrayList());
                totalLabel.setText("Total: $0.00");
                updateBalanceInProfile( balanceLabel ,customer.getBalance());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Checkout failed: " + ex.getMessage());
            }
        });

        Button editProfileButton = new Button("Edit Profile");
        Button addBalanceButton = new Button("Add Balance");

        editProfileButton.setOnAction(e -> showEditProfileDialog(customer,profileLayout));
        addBalanceButton.setOnAction(e -> showAddBalanceDialog(customer, balanceLabel));

        HBox profileButtons = new HBox(10);
        profileButtons.setAlignment(Pos.CENTER);
        profileButtons.getChildren().addAll(editProfileButton, addBalanceButton);
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            primaryStage.setScene(loginScene);
        });

        profileLayout.getChildren().addAll(
                usernameLabel,
                balanceLabel,
                addressLabel,
                genderLabel,
                interestsLabel,
                profileButtons,
                logoutButton
        );
        profileTab.setContent(profileLayout);

        tabPane.getTabs().addAll(productsTab, cartTab, ordersTab, profileTab);
        customerDashboard = new Scene(tabPane, 800, 600);

    }
    private void updateBalanceInProfile(Label balanceLabel, double updatedBalance) {
        balanceLabel.setText(String.format("Balance: $%.2f", updatedBalance));
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateCartDisplay(TableView<Cart.CartItem> cartTable, Customer customer, Label totalLabel) {
        Cart.CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        ObservableList<Cart.CartItem> cartItems = FXCollections.observableArrayList(customer.getCart().getItems());
        cartTable.getColumns().clear();
        TableColumn<Cart.CartItem, String> cartNameCol = new TableColumn<>("Name");
        cartNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getName())
        );

        TableColumn<Cart.CartItem, Double> cartPriceCol = new TableColumn<>("Price");
        cartPriceCol.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject()
        );

        TableColumn<Cart.CartItem, Integer> cartQuantityCol = new TableColumn<>("Quantity");
        cartQuantityCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );

        cartTable.getColumns().addAll(cartNameCol, cartPriceCol, cartQuantityCol);

        cartTable.setItems(cartItems);
        if (selectedItem != null && cartItems.contains(selectedItem)) {
            cartTable.getSelectionModel().select(selectedItem);
        }
        double totalPrice = customer.getCart().calculateTotalPrice();
        totalLabel.setText(String.format("Total: $%.2f", totalPrice));
    }


    private void showOrderDetailsDialog(Order order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Order Details");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<Cart.CartItem> productsTable = new TableView<>();
        TableColumn<Cart.CartItem, String> nameCol = new TableColumn<>("Product");
        TableColumn<Cart.CartItem, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Cart.CartItem, Integer> quantityCol = new TableColumn<>("Quantity");

        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        priceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        productsTable.getColumns().addAll(nameCol, priceCol, quantityCol);

        if (order.getItems() == null || order.getItems().isEmpty()) {
            System.out.println("Order has no items.");
        } else {
            productsTable.setItems(FXCollections.observableArrayList(order.getItems()));
        }

        Label totalLabel = new Label(String.format("Total: $%.2f", order.getTotalPrice()));
        Label statusLabel = new Label("Status: " + order.getStatus());
        Label dateLabel = new Label("Order Date: " + order.getOrderDate());

        content.getChildren().addAll(
                productsTable,
                totalLabel,
                statusLabel,
                dateLabel
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    private void updateOrderTable(TableView<Order> orderTable, Customer customer) {
        ObservableList<Order> orders = FXCollections.observableArrayList(customer.getOrders());
        orderTable.setItems(orders);
    }


    private void showEditProfileDialog(Customer customer, VBox profileLayout) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer Profile");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField addressField = new TextField(customer.getAddress());
        TextField interestsField = new TextField(String.join(", ", customer.getInterests()));
        ComboBox<Customer.Gender> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll(Customer.Gender.values());
        genderCombo.setValue(customer.getGender());

        grid.add(new Label("Address:"), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(new Label("Gender:"), 0, 1);
        grid.add(genderCombo, 1, 1);
        grid.add(new Label("Interests:"), 0, 2);
        grid.add(interestsField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    if (addressField.getText().trim().isEmpty()) {
                        throw new IllegalArgumentException("Address cannot be empty");
                    }

                    customer.setAddress(addressField.getText().trim());
                    customer.setGender(genderCombo.getValue());
                    customer.setInterests(Arrays.asList(interestsField.getText().split("\\s*,\\s*")));

                    refreshCustomerProfileTab(customer, profileLayout);

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                    return true;
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    return false;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile: " + e.getMessage());
                    return false;
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showAddBalanceDialog(Customer customer, Label balanceLabel) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Balance");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");

        content.getChildren().addAll(
                new Label("Amount to add:"),
                amountField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    customer.addBalance(amount);
                    balanceLabel.setText("Balance: $" + customer.getBalance());
                    return true;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid amount");
                } catch (IllegalArgumentException e){
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return false;
        });

        dialog.showAndWait();
    }
    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.setItems(FXCollections.observableArrayList(Database.categories));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    return new Product(
                            nameField.getText(),
                            descriptionField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(quantityField.getText()),
                            categoryCombo.getValue()
                    );
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditProductDialog(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(product.getName());
        TextField descriptionField = new TextField(product.getDescription());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getQuantity()));
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.setItems(FXCollections.observableArrayList(Database.categories));
        categoryCombo.setValue(product.getCategory());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    product.setName(nameField.getText());
                    product.setDescription(descriptionField.getText());
                    product.setPrice(Double.parseDouble(priceField.getText()));
                    product.setQuantity(Integer.parseInt(quantityField.getText()));
                    product.setCategory(categoryCombo.getValue());
                    Admin.updateProduct(product);
                    return product;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAddCategoryDialog() {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Add Category");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    return new Category(nameField.getText());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditCategoryDialog(Category category) {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Edit Category");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(category.getName());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    category.setName(nameField.getText());
                    Admin.updateCategory(category);
                    return category;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showUpdateOrderStatusDialog(Order order) {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Update Order Status");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        ComboBox<Order.OrderStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Order.OrderStatus.values());
        statusCombo.setValue(order.getStatus());

        content.getChildren().addAll(
                new Label("Status:"),
                statusCombo
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    order.setStatus(statusCombo.getValue());
                    return order;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void showEditAdminProfileDialog(Admin admin, Label roleLabel, Label workingHoursLabel) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Admin Profile");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField roleField = new TextField(admin.getRole());
        TextField workingHoursField = new TextField(String.valueOf(admin.getWorkingHours()));

        grid.add(new Label("Role:"), 0, 0);
        grid.add(roleField, 1, 0);
        grid.add(new Label("Working Hours:"), 0, 1);
        grid.add(workingHoursField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    admin.setRole(roleField.getText());
                    admin.setWorkingHours(Integer.parseInt(workingHoursField.getText()));
                    roleLabel.setText("Role: " + admin.getRole());
                    workingHoursLabel.setText("Working Hours: " + admin.getWorkingHours());

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                    return true;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for working hours");
                } catch (IllegalArgumentException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return false;
        });

        dialog.showAndWait();
    }
    private void addSearchFunctionality(TableView<Product> productTable, VBox layout) {
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(300);

        ComboBox<String> searchCriteria = new ComboBox<>();
        searchCriteria.getItems().addAll("All", "Name", "Description", "Category");
        searchCriteria.setValue("All");

        searchBox.getChildren().addAll(new Label("Search by:"), searchCriteria, searchField);
        layout.getChildren().addFirst(searchBox);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String criteria = searchCriteria.getValue();
            String searchText = newValue.toLowerCase();

            ObservableList<Product> filteredList = FXCollections.observableArrayList(
                    Database.products.stream()
                            .filter(product -> {
                                if (searchText.isEmpty()) {
                                    return true;
                                }

                                return switch (criteria) {
                                    case "Name" -> product.getName().toLowerCase().contains(searchText);
                                    case "Description" -> product.getDescription().toLowerCase().contains(searchText);
                                    case "Category" ->
                                            product.getCategory().getName().toLowerCase().contains(searchText);
                                    default -> product.getName().toLowerCase().contains(searchText) ||
                                            product.getDescription().toLowerCase().contains(searchText) ||
                                            product.getCategory().getName().toLowerCase().contains(searchText);
                                };
                            })
                            .collect(Collectors.toList())
            );

            productTable.setItems(filteredList);
        });
    }
    private Tab createCustomersTab() {
        Tab customersTab = new Tab("Customers");
        customersTab.setClosable(false);
        VBox customersLayout = new VBox(10);
        customersLayout.setPadding(new Insets(10));

        TableView<Customer> customerTable = new TableView<>();

        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));

        TableColumn<Customer, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, Customer.Gender> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Customer, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Customer, String> interestsCol = new TableColumn<>("Interests");
        interestsCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.join(", ", cellData.getValue().getInterests())));

        TableColumn<Customer, Integer> ordersCol = new TableColumn<>("Total Orders");
        ordersCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getOrders().size()).asObject());

        customerTable.getColumns().addAll(
                usernameCol, dobCol, addressCol, genderCol,
                balanceCol, interestsCol, ordersCol
        );
        customerTable.setItems(FXCollections.observableArrayList(Database.customers));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Search customers...");
        searchField.setPrefWidth(300);

        ComboBox<String> searchCriteria = new ComboBox<>();
        searchCriteria.getItems().addAll("All", "Username", "Address", "Interests");
        searchCriteria.setValue("All");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String criteria = searchCriteria.getValue();
            String searchText = newValue.toLowerCase();

            ObservableList<Customer> filteredList = FXCollections.observableArrayList(
                    Database.customers.stream()
                            .filter(customer -> {
                                if (searchText.isEmpty()) {
                                    return true;
                                }

                                return switch (criteria) {
                                    case "Username" -> customer.getUserName().toLowerCase().contains(searchText);
                                    case "Address" -> customer.getAddress().toLowerCase().contains(searchText);
                                    case "Interests" -> customer.getInterests().stream()
                                            .anyMatch(interest -> interest.toLowerCase().contains(searchText));
                                    default ->
                                            customer.getUserName().toLowerCase().contains(searchText) ||
                                                    customer.getAddress().toLowerCase().contains(searchText) ||
                                                    customer.getInterests().stream()
                                                            .anyMatch(interest -> interest.toLowerCase().contains(searchText));
                                };
                            })
                            .collect(Collectors.toList())
            );

            customerTable.setItems(filteredList);
        });

        Button viewOrdersButton = new Button("View Orders");
        viewOrdersButton.setOnAction(e -> {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                showCustomerOrdersDialog(selectedCustomer);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer");
            }
        });

        searchBox.getChildren().addAll(new Label("Search by:"), searchCriteria, searchField);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(viewOrdersButton);

        customersLayout.getChildren().addAll(searchBox, customerTable, buttonBox);
        customersTab.setContent(customersLayout);

        return customersTab;
    }

    private void showCustomerOrdersDialog(Customer customer) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Orders for " + customer.getUserName());
        dialog.setHeaderText("Order History");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<Order> orderTable = new TableView<>();

        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        orderTable.getColumns().addAll(orderIdCol, totalCol, statusCol, dateCol);
        orderTable.setItems(FXCollections.observableArrayList(customer.getOrders()));

        Button viewDetailsButton = new Button("View Order Details");
        viewDetailsButton.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showOrderDetailsDialog(selectedOrder);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select an order");
            }
        });

        content.getChildren().addAll(
                new Label("Customer: " + customer.getUserName()),
                new Label("Total Orders: " + customer.getOrders().size()),
                orderTable,
                viewDetailsButton
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.show();
    }
    private void refreshCustomerProfileTab(Customer customer, VBox profileLayout) {
        profileLayout.getChildren().clear();
        Label usernameLabel = new Label("Username: " + customer.getUserName());
        Label balanceLabel = new Label(String.format("Balance: $%.2f", customer.getBalance()));
        Label addressLabel = new Label("Address: " + customer.getAddress());
        Label genderLabel = new Label("Gender: " + customer.getGender());
        Label interestsLabel = new Label("Interests: " + String.join(", ", customer.getInterests()));

        Button editProfileButton = new Button("Edit Profile");
        Button addBalanceButton = new Button("Add Balance");

        editProfileButton.setOnAction(e -> showEditProfileDialog(customer, profileLayout));
        addBalanceButton.setOnAction(e -> showAddBalanceDialog(customer, balanceLabel));

        HBox profileButtons = new HBox(10);
        profileButtons.setAlignment(Pos.CENTER);
        profileButtons.getChildren().addAll(editProfileButton, addBalanceButton);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            currentUser = null;
            primaryStage.setScene(loginScene);
        });

        profileLayout.getChildren().addAll(
                usernameLabel,
                balanceLabel,
                addressLabel,
                genderLabel,
                interestsLabel,
                profileButtons,
                logoutButton
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}