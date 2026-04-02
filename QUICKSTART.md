# Quick Start Guide - Café Ordering App

## 🚀 Getting Started in 5 Steps

### Step 1: Open Project
1. Launch Android Studio
2. Click **File → Open**
3. Navigate to: `C:\Users\Source Tech Co\AndroidStudioProjects\projectyear`
4. Click **Open**

### Step 2: Sync Gradle
1. Wait for Android Studio to recognize the project
2. Click **File → Sync Now** (if prompted)
3. Wait for Gradle build to complete (bottom status bar)

### Step 3: Create/Select Emulator
1. Click **Tools → Device Manager**
2. Create a new emulator (if none exists)
   - Click **Create Device**
   - Select "Pixel 5" or "Pixel 6"
   - Select API level 30 or higher
   - Click **Finish**
3. Select your emulator from the device list

### Step 4: Run Application
1. Click **Run → Run 'app'** (or press Shift+F10)
2. Select your emulator
3. Click **OK**
4. Wait for app to build and deploy (~2-3 minutes first time)

### Step 5: Test the App
1. **Splash Screen** appears for 3 seconds
2. **Login Screen** appears
3. Enter any email (e.g., `user@example.com`)
4. Enter any password (e.g., `password123`)
5. Click **CREATE NEW ACCOUNT** (first time)
6. You'll be logged in and see the **Home Screen**

---

## 🎯 Feature Walk-Through

### Home Screen
```
┌─────────────────────────────────┐
│    Welcome to Café Ordering     │
│   Browse our delicious menu     │
│                                 │
│  [📋 Browse Menu]              │
│  [📦 My Orders]                │
│  [👤 My Profile]               │
│  [🚪 Logout]                   │
└─────────────────────────────────┘
```

**Actions:**
- **Browse Menu** → See all café items
- **My Orders** → View order history
- **My Profile** → See user info and orders
- **Logout** → Sign out (requires login again)

### Menu Screen
```
┌─────────────────────────────────┐
│          Our Menu               │
│                                 │
│  ┌─ COFFEE ─────────────────┐  │
│  │ ☕ Espresso - Rs. 150    │  │
│  │    Rich and strong coffee │  │
│  │    [Add to Cart]         │  │
│  └──────────────────────────┘  │
│                                 │
│  ┌─ Latte - Rs. 180 ─────────┐ │
│  │ Smooth coffee with milk   │ │
│  │ [Add to Cart]             │ │
│  └───────────────────────────┘ │
│                                 │
│  [🛒 Go to Cart]              │
└─────────────────────────────────┘
```

**Actions:**
- Scroll through menu items
- Click **Add to Cart** on any item
- See "Item added to cart!" toast message
- Click **Go to Cart** when done shopping

### Cart Screen
```
┌─────────────────────────────────┐
│         Your Cart               │
│                                 │
│  Cappuccino      Rs. 200 × 2    │
│  Espresso        Rs. 150 × 1    │
│  Brownie         Rs. 250 × 1    │
│                                 │
├─────────────────────────────────┤
│  Subtotal:        Rs. 800.00    │
│  Tax (5%):        Rs. 40.00     │
│  ─────────────────────────────  │
│  TOTAL:           Rs. 840.00    │
│                                 │
│  [Continue Shopping] [Checkout] │
└─────────────────────────────────┘
```

**Actions:**
- View items with quantities
- See real-time total calculation
- **Continue Shopping** → Back to menu
- **Checkout** → Place order

### Order Confirmation
```
┌─────────────────────────────────┐
│  Order Confirmed! ✓             │
│                                 │
│  Order Details                  │
│  Order ID: 1                    │
│  Time: 01 Jan 2024, 14:30      │
│  Status: CONFIRMED              │
│                                 │
│  Total: Rs. 840.00              │
│                                 │
│  Thank you for your order!      │
│  Estimated ready: 15 minutes    │
│                                 │
│  [Back to Home]                │
└─────────────────────────────────┘
```

**Info:**
- Order automatically saved to database
- Timestamp recorded
- Order ID assigned
- Cart cleared for next order

### Profile Screen
```
┌─────────────────────────────────┐
│        My Profile               │
│                                 │
│  Email: user@example.com        │
│                                 │
│  Order History                  │
│  ┌──────────────────────────┐  │
│  │ Order #1                 │  │
│  │ 01 Jan 2024, 14:30      │  │
│  │ Rs. 840.00               │  │
│  │ Status: CONFIRMED        │  │
│  └──────────────────────────┘  │
│                                 │
│  [Back to Home]                │
└─────────────────────────────────┘
```

**Info:**
- See your account email
- View all previous orders
- See order dates and amounts

---

## 🧪 Quick Test Scenarios

### Scenario 1: New User Sign-Up
```
1. Splash screen displays (3 sec)
2. Login page appears
3. Enter: email = "john@cafe.com", password = "pass123"
4. Click "CREATE NEW ACCOUNT"
5. Account created → Home screen
✓ Success: User logged in
```

### Scenario 2: Browse and Add to Cart
```
1. From home, click "📋 Browse Menu"
2. Menu screen shows coffee, tea, desserts
3. Click "Add to Cart" on "Cappuccino"
4. Toast: "Cappuccino added to cart!"
5. Click "Add to Cart" on "Espresso"
6. Toast: "Espresso added to cart!"
✓ Success: 2 items in cart
```

### Scenario 3: Complete Order
```
1. Click "🛒 Go to Cart"
2. See cart with 2 items, total price
3. Click "Proceed to Checkout"
4. Confirmation shows Order ID & timestamp
5. Click "Back to Home"
6. Cart is now empty
✓ Success: Order placed and saved
```

### Scenario 4: View Order History
```
1. From home, click "👤 My Profile"
2. See email address
3. See "Order History" with placed order
4. Order shows ID, date, amount, status
✓ Success: Order saved in database
```

### Scenario 5: Logout and Re-Login
```
1. From home, click "🚪 Logout"
2. Splash screen appears
3. Login screen appears (can't go to home)
4. Enter same email and password
5. Click "LOGIN"
✓ Success: Logged back in with existing account
```

---

## 📊 Sample Test Data

### Pre-loaded Menu Items
```
COFFEE:
- Espresso (Rs. 150)
- Latte (Rs. 180)
- Cappuccino (Rs. 200)
- Americano (Rs. 160)
- Mocha (Rs. 220)

TEA:
- Green Tea (Rs. 120)
- Black Tea (Rs. 130)
- Chamomile (Rs. 140)

DESSERTS:
- Chocolate Brownie (Rs. 250)
- Croissant (Rs. 180)
- Cheesecake (Rs. 300)
```

### Test Accounts
You can create any account with:
- Email: Any valid email format (e.g., test@email.com)
- Password: Any password (e.g., password123)

Multiple accounts can be created and tracked separately.

---

## 🐛 Troubleshooting

### Issue: Gradle Sync Fails
**Solution:**
1. Click **File → Invalidate Caches**
2. Select **Invalidate and Restart**
3. Wait for restart and re-sync

### Issue: Emulator Won't Start
**Solution:**
1. Click **Tools → Device Manager**
2. Click the play icon to start emulator
3. Wait 1-2 minutes for emulator to boot
4. Try running app again

### Issue: App Crashes on Launch
**Solution:**
1. Click **Run → Clean Project**
2. Click **Run → Run 'app'** again
3. If still crashes, check logcat (bottom panel) for errors

### Issue: No Menu Items Showing
**Solution:**
- Menu items auto-populate on first MainActivity load
- Wait 2-3 seconds for database to initialize
- Scroll down if menu items are below visible area

---

## 📱 Screen Navigation Map

```
SplashActivity (3 sec auto-delay)
        ↓
    LoginActivity ← (if not logged in)
        ↓ (Login/Signup)
    MainActivity (HOME)
        ├→ MenuActivity
        │   ├→ MenuFragment (RecyclerView)
        │   └→ CartActivity
        │       └→ OrderConfirmationActivity
        │           └→ MainActivity
        ├→ ProfileActivity
        │   └→ MainActivity
        └→ Logout → LoginActivity
```

---

## 🎨 UI Color Reference

- **Brown Headers/Buttons**: Used in primary actions
- **Cream/Beige**: Background and secondary buttons
- **White**: Card surfaces
- **Dark Brown**: Text and accents
- **Green**: Proceed/Success actions

---

## 📊 Database Info

All data stored locally in SQLite database:
- User accounts
- Menu items (11 total, pre-loaded)
- Orders (saved when placed)
- Order items (links orders to menu items)

Database file location: 
`/data/data/com.example.projectyear/databases/cafe_database`

---

## ✨ Tips & Tricks

1. **Quick Add Multiple Items**
   - Go to menu, click Add to Cart multiple times
   - Quantities auto-increment

2. **Update Quantities**
   - In cart, can modify item quantities

3. **View Order Details**
   - Click on an order in order history to see more details

4. **Test Multiple Accounts**
   - Create 2+ accounts to test multi-user functionality
   - Each user has separate order history

5. **Check Timestamps**
   - Order confirmation shows exact time
   - Useful for real-time ordering validation

---

## 📞 Key Components

| Component | Location | Purpose |
|-----------|----------|---------|
| SplashActivity | Java file | Splash screen & routing |
| LoginActivity | Java file | Auth & account creation |
| MainActivity | Java file | Home hub |
| MenuFragment | Java file | Menu display (reusable) |
| Cart | Java file | Session cart management |
| CafeDatabase | Java file | Room database |
| Adapters | Java files | RecyclerView logic |

---

## 🎓 What You'll Learn

This app demonstrates:
- ✅ Activity lifecycle & navigation
- ✅ Fragment usage with RecyclerView
- ✅ Room database operations
- ✅ Material Design UI
- ✅ SharedPreferences for sessions
- ✅ Background threading
- ✅ Responsive layouts
- ✅ Real-time calculations

---

## 🚀 You're All Set!

The app is ready to run. Follow Step 4 above and enjoy testing the Café Ordering App!

**Happy Coding! ☕**

---

*Last Updated: April 1, 2026*

