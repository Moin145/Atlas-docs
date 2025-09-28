// Banking System Frontend JavaScript
const API_BASE_URL = 'http://localhost:8080/api';

// Global state
let currentUser = 'WEB_USER';
let customers = [];
let accounts = [];
let transactions = [];
let auditLogs = [];

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    loadDashboardData();
    // Defensive: ensure no modal is shown by default due to cached state or browser restore
    forceCloseAllModals();

    // Theme toggle
    const themeBtn = document.getElementById('themeToggleBtn');
    const savedTheme = localStorage.getItem('bts-theme');
    if (savedTheme === 'light') {
        document.body.classList.add('light');
    }
    if (themeBtn) {
        themeBtn.addEventListener('click', () => {
            document.body.classList.toggle('light');
            localStorage.setItem('bts-theme', document.body.classList.contains('light') ? 'light' : 'dark');
        });
    }
});

// Initialize application
function initializeApp() {
    console.log('🏦 Indian Banking System - Web Interface Initialized');
    showSection('dashboard');
}

// Setup event listeners
function setupEventListeners() {
    // Form submissions
    document.getElementById('depositForm').addEventListener('submit', handleDeposit);
    document.getElementById('withdrawForm').addEventListener('submit', handleWithdraw);
    document.getElementById('transferForm').addEventListener('submit', handleTransfer);
    document.getElementById('createCustomerForm').addEventListener('submit', handleCreateCustomer);
    document.getElementById('createAccountForm').addEventListener('submit', handleCreateAccount);
}

// Navigation functions
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remove active class from all nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(sectionId).classList.add('active');
    
    // Add active class to corresponding nav item
    const navItem = document.querySelector(`[onclick="showSection('${sectionId}')"]`);
    if (navItem) {
        navItem.classList.add('active');
    }
    
    // Load section-specific data
    switch(sectionId) {
        case 'customers':
            loadCustomers();
            break;
        case 'accounts':
            loadAccounts();
            break;
        case 'audit':
            loadAuditLogs();
            break;
    }
}

// Ensure modals are hidden on initial load
function forceCloseAllModals() {
    const modalIds = ['createCustomerModal', 'createAccountModal'];
    modalIds.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.style.display = 'none';
    });
}

// API Helper Functions
async function apiCall(endpoint, method = 'GET', data = null) {
    const config = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        }
    };
    
    if (data) {
        config.body = JSON.stringify(data);
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.message || 'API call failed');
        }
        
        return result;
    } catch (error) {
        console.error('API Error:', error);
        
        // Don't show notification for every API call failure to avoid spam
        if (error.message.includes('Failed to fetch') || error.message.includes('Connection refused')) {
            throw new Error('Backend server is not running. Please start the backend on http://localhost:8080');
        }
        
        throw error;
    }
}

// Dashboard functions
async function loadDashboardData() {
    try {
        showLoading(true);
        
        // Load customers
        try {
            const customersResult = await apiCall('/customers');
            customers = customersResult.customers || [];
        } catch (error) {
            console.warn('Could not load customers:', error.message);
            customers = [];
            showNotification('⚠️ Could not connect to backend API. Please ensure the backend is running on http://localhost:8080', 'warning');
        }
        
        // Load accounts
        try {
            const accountsResult = await apiCall('/accounts');
            accounts = accountsResult.accounts || [];
        } catch (error) {
            console.warn('Could not load accounts:', error.message);
            accounts = [];
        }
        
        // Update dashboard stats
        updateDashboardStats();
        
        // Load recent activity
        loadRecentActivity();
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showNotification('⚠️ Dashboard data could not be loaded. Please check if the backend is running.', 'warning');
    } finally {
        showLoading(false);
    }
}

function updateDashboardStats() {
    document.getElementById('totalCustomers').textContent = customers.length;
    document.getElementById('totalAccounts').textContent = accounts.length;
    
    const totalBalance = accounts.reduce((sum, account) => {
        return sum + parseFloat(account.balance || 0);
    }, 0);
    
    document.getElementById('totalBalance').textContent = `₹${totalBalance.toLocaleString('en-IN')}`;
    document.getElementById('totalTransactions').textContent = transactions.length;
}

function loadRecentActivity() {
    const activityContainer = document.getElementById('recentActivity');
    activityContainer.innerHTML = '';
    
    // Sample recent activity (in real app, this would come from API)
    const activities = [
        {
            icon: 'fas fa-plus-circle',
            title: 'New Account Created',
            description: 'Savings account for Rajesh Kumar',
            time: '2 minutes ago'
        },
        {
            icon: 'fas fa-exchange-alt',
            title: 'Money Transfer',
            description: '₹5,000 transferred between accounts',
            time: '15 minutes ago'
        },
        {
            icon: 'fas fa-plus-circle',
            title: 'Deposit Made',
            description: '₹10,000 deposited to account ACC123456',
            time: '1 hour ago'
        }
    ];
    
    activities.forEach(activity => {
        const activityElement = document.createElement('div');
        activityElement.className = 'activity-item';
        activityElement.innerHTML = `
            <div class="activity-icon">
                <i class="${activity.icon}"></i>
            </div>
            <div class="activity-content">
                <h4>${activity.title}</h4>
                <p>${activity.description} • ${activity.time}</p>
            </div>
        `;
        activityContainer.appendChild(activityElement);
    });
}

// Customer functions
async function loadCustomers() {
    try {
        showLoading(true);
        const result = await apiCall('/customers');
        customers = result.customers || [];
        displayCustomers(customers);
    } catch (error) {
        console.error('Error loading customers:', error);
    } finally {
        showLoading(false);
    }
}

function displayCustomers(customersList) {
    const tbody = document.getElementById('customersTableBody');
    tbody.innerHTML = '';
    
    customersList.forEach(customer => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${customer.id}</td>
            <td>${customer.firstName} ${customer.lastName}</td>
            <td>${customer.email}</td>
            <td>${customer.mobileNumber}</td>
            <td>${customer.city}</td>
            <td>
                <button class="btn btn-primary" onclick="viewCustomer('${customer.id}')">
                    <i class="fas fa-eye"></i> View
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showCreateCustomerForm() {
    document.getElementById('createCustomerModal').style.display = 'block';
}

async function handleCreateCustomer(event) {
    event.preventDefault();
    
    try {
        showLoading(true);
        
        const formData = new FormData(event.target);
        const customerData = {
            firstName: formData.get('firstName'),
            lastName: formData.get('lastName'),
            email: formData.get('email'),
            mobileNumber: formData.get('mobileNumber'),
            address: formData.get('address'),
            city: formData.get('city'),
            state: formData.get('state'),
            pincode: formData.get('pincode'),
            userId: currentUser
        };
        
        const result = await apiCall('/customers', 'POST', customerData);
        showNotification('Customer created successfully!', 'success');
        closeModal('createCustomerModal');
        event.target.reset();
        loadCustomers();
        
    } catch (error) {
        console.error('Error creating customer:', error);
    } finally {
        showLoading(false);
    }
}

// Account functions
async function loadAccounts() {
    try {
        showLoading(true);
        const result = await apiCall('/accounts');
        accounts = result.accounts || [];
        displayAccounts(accounts);
    } catch (error) {
        console.error('Error loading accounts:', error);
    } finally {
        showLoading(false);
    }
}

function displayAccounts(accountsList) {
    const tbody = document.getElementById('accountsTableBody');
    tbody.innerHTML = '';
    
    accountsList.forEach(account => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${account.accountNumber}</td>
            <td>${account.customer ? account.customer.firstName + ' ' + account.customer.lastName : 'N/A'}</td>
            <td>${account.accountType}</td>
            <td>₹${parseFloat(account.balance || 0).toLocaleString('en-IN')}</td>
            <td>${account.status}</td>
            <td>
                <button class="btn btn-primary" onclick="viewAccount('${account.accountNumber}')">
                    <i class="fas fa-eye"></i> View
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function showCreateAccountForm() {
    document.getElementById('createAccountModal').style.display = 'block';
}

async function handleCreateAccount(event) {
    event.preventDefault();
    
    try {
        showLoading(true);
        
        const formData = new FormData(event.target);
        const accountData = {
            customerId: formData.get('customerId'),
            accountType: formData.get('accountType'),
            userId: currentUser
        };
        
        const result = await apiCall('/accounts', 'POST', accountData);
        showNotification('Account created successfully!', 'success');
        closeModal('createAccountModal');
        event.target.reset();
        loadAccounts();
        
    } catch (error) {
        console.error('Error creating account:', error);
    } finally {
        showLoading(false);
    }
}

// Transaction functions
async function handleDeposit(event) {
    event.preventDefault();
    
    try {
        showLoading(true);
        
        const formData = new FormData(event.target);
        const depositData = {
            accountNumber: formData.get('accountNumber'),
            amount: parseFloat(formData.get('amount')),
            description: formData.get('description'),
            userId: currentUser
        };
        
        const result = await apiCall('/transactions/deposit', 'POST', depositData);
        showNotification('Deposit processed successfully!', 'success');
        event.target.reset();
        loadDashboardData();
        
    } catch (error) {
        console.error('Error processing deposit:', error);
    } finally {
        showLoading(false);
    }
}

async function handleWithdraw(event) {
    event.preventDefault();
    
    try {
        showLoading(true);
        
        const formData = new FormData(event.target);
        const withdrawData = {
            accountNumber: formData.get('accountNumber'),
            amount: parseFloat(formData.get('amount')),
            description: formData.get('description'),
            userId: currentUser
        };
        
        const result = await apiCall('/transactions/withdraw', 'POST', withdrawData);
        showNotification('Withdrawal processed successfully!', 'success');
        event.target.reset();
        loadDashboardData();
        
    } catch (error) {
        console.error('Error processing withdrawal:', error);
    } finally {
        showLoading(false);
    }
}

async function handleTransfer(event) {
    event.preventDefault();
    
    try {
        showLoading(true);
        
        const formData = new FormData(event.target);
        const transferData = {
            sourceAccountNumber: formData.get('sourceAccountNumber'),
            destinationAccountNumber: formData.get('destinationAccountNumber'),
            amount: parseFloat(formData.get('amount')),
            description: formData.get('description'),
            userId: currentUser
        };
        
        const result = await apiCall('/transactions/transfer', 'POST', transferData);
        showNotification('Transfer processed successfully!', 'success');
        event.target.reset();
        loadDashboardData();
        
    } catch (error) {
        console.error('Error processing transfer:', error);
    } finally {
        showLoading(false);
    }
}

// Transaction History functions
async function loadTransactionHistory() {
    const accountNumber = document.getElementById('historyAccountNumber').value;
    
    if (!accountNumber) {
        showNotification('Please enter an account number', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const result = await apiCall(`/transactions/account/${accountNumber}`);
        transactions = result.transactions || [];
        displayTransactionHistory(transactions);
    } catch (error) {
        console.error('Error loading transaction history:', error);
    } finally {
        showLoading(false);
    }
}

function displayTransactionHistory(transactionsList) {
    const tbody = document.getElementById('historyTableBody');
    tbody.innerHTML = '';
    
    if (transactionsList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No transactions found</td></tr>';
        return;
    }
    
    transactionsList.forEach(transaction => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${transaction.transactionId}</td>
            <td>${transaction.type}</td>
            <td>₹${parseFloat(transaction.amount || 0).toLocaleString('en-IN')}</td>
            <td>${transaction.description}</td>
            <td>${transaction.status}</td>
            <td>${new Date(transaction.transactionDate).toLocaleString('en-IN')}</td>
        `;
        tbody.appendChild(row);
    });
}

// Undo/Redo functions
async function checkUndoRedoStatus() {
    const accountId = document.getElementById('undoRedoAccountId').value;
    
    if (!accountId) {
        showNotification('Please enter an account ID', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const result = await apiCall(`/transactions/account/${accountId}/undo-redo-status`);
        
        const statusDiv = document.getElementById('undoRedoStatus');
        statusDiv.innerHTML = `
            <h4>Undo/Redo Status</h4>
            <p><strong>Can Undo:</strong> ${result.canUndo ? 'Yes' : 'No'}</p>
            <p><strong>Can Redo:</strong> ${result.canRedo ? 'Yes' : 'No'}</p>
            <p><strong>Undo Stack Size:</strong> ${result.undoStackSize}</p>
            <p><strong>Redo Stack Size:</strong> ${result.redoStackSize}</p>
        `;
        
        document.getElementById('undoBtn').disabled = !result.canUndo;
        document.getElementById('redoBtn').disabled = !result.canRedo;
        
    } catch (error) {
        console.error('Error checking undo/redo status:', error);
    } finally {
        showLoading(false);
    }
}

async function undoTransaction() {
    const accountId = document.getElementById('undoRedoAccountId').value;
    
    if (!accountId) {
        showNotification('Please enter an account ID', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const result = await apiCall(`/transactions/undo/${accountId}`, 'POST', { userId: currentUser });
        showNotification('Transaction undone successfully!', 'success');
        checkUndoRedoStatus();
        
    } catch (error) {
        console.error('Error undoing transaction:', error);
    } finally {
        showLoading(false);
    }
}

async function redoTransaction() {
    const accountId = document.getElementById('undoRedoAccountId').value;
    
    if (!accountId) {
        showNotification('Please enter an account ID', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const result = await apiCall(`/transactions/redo/${accountId}`, 'POST', { userId: currentUser });
        showNotification('Transaction redone successfully!', 'success');
        checkUndoRedoStatus();
        
    } catch (error) {
        console.error('Error redoing transaction:', error);
    } finally {
        showLoading(false);
    }
}

// Audit Log functions
async function loadAuditLogs() {
    try {
        showLoading(true);
        const result = await apiCall('/audit/logs');
        auditLogs = result.logs || [];
        displayAuditLogs(auditLogs);
    } catch (error) {
        console.error('Error loading audit logs:', error);
    } finally {
        showLoading(false);
    }
}

function displayAuditLogs(logsList) {
    const tbody = document.getElementById('auditTableBody');
    tbody.innerHTML = '';
    
    if (logsList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No audit logs found</td></tr>';
        return;
    }
    
    logsList.forEach(log => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(log.timestamp).toLocaleString('en-IN')}</td>
            <td>${log.userId}</td>
            <td>${log.action}</td>
            <td>${log.entityType}</td>
            <td>${log.result}</td>
            <td>${log.description || 'N/A'}</td>
        `;
        tbody.appendChild(row);
    });
}

// Utility functions
function showLoading(show) {
    const spinner = document.getElementById('loadingSpinner');
    spinner.style.display = show ? 'flex' : 'none';
}

function showNotification(message, type = 'info') {
    const notification = document.getElementById('notification');
    const messageElement = document.getElementById('notificationMessage');
    
    // Remove existing type classes
    notification.classList.remove('warning', 'error', 'success');
    
    // Add the appropriate type class
    if (type !== 'info') {
        notification.classList.add(type);
    }
    
    messageElement.textContent = message;
    notification.style.display = 'flex';
    
    // Auto hide after 5 seconds
    setTimeout(() => {
        hideNotification();
    }, 5000);
}

function hideNotification() {
    document.getElementById('notification').style.display = 'none';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Close modals when clicking outside
window.onclick = function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// View functions (placeholder implementations)
function viewCustomer(customerId) {
    const customer = customers.find(c => c.id === customerId);
    if (customer) {
        alert(`Customer Details:\nName: ${customer.firstName} ${customer.lastName}\nEmail: ${customer.email}\nMobile: ${customer.mobileNumber}\nAddress: ${customer.address}, ${customer.city}, ${customer.state} ${customer.pincode}`);
    }
}

function viewAccount(accountNumber) {
    const account = accounts.find(a => a.accountNumber === accountNumber);
    if (account) {
        alert(`Account Details:\nAccount Number: ${account.accountNumber}\nType: ${account.accountType}\nBalance: ₹${parseFloat(account.balance || 0).toLocaleString('en-IN')}\nStatus: ${account.status}\nCustomer: ${account.customer ? account.customer.firstName + ' ' + account.customer.lastName : 'N/A'}`);
    }
}

// Data Sync Functions
async function syncDataToDynamoDB() {
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE_URL}/sync/mongodb-to-dynamodb`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (result.success) {
            showNotification('✅ Data synced to DynamoDB successfully!', 'success');
            updateSyncStatus(result);
        } else {
            showNotification('❌ Sync failed: ' + result.message, 'error');
        }
        
    } catch (error) {
        console.error('Error syncing data:', error);
        showNotification('❌ Error syncing data: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function checkSyncStatus() {
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE_URL}/sync/status`);
        const result = await response.json();
        
        if (result.success) {
            updateSyncStatus(result);
            showNotification('✅ Sync status checked successfully!', 'success');
        } else {
            showNotification('❌ Failed to check sync status', 'error');
        }
        
    } catch (error) {
        console.error('Error checking sync status:', error);
        showNotification('❌ Error checking sync status: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

function updateSyncStatus(result) {
    const statusDiv = document.getElementById('syncStatus');
    statusDiv.innerHTML = `
        <h4>Sync Status</h4>
        <p><strong>Status:</strong> ${result.success ? '✅ Success' : '❌ Failed'}</p>
        <p><strong>Message:</strong> ${result.message}</p>
        <p><strong>Timestamp:</strong> ${result.timestamp}</p>
        ${result.mongodbCollections ? `
            <p><strong>MongoDB Collections:</strong> ${result.mongodbCollections.join(', ')}</p>
        ` : ''}
        ${result.dynamodbTables ? `
            <p><strong>DynamoDB Tables:</strong> ${result.dynamodbTables.join(', ')}</p>
        ` : ''}
    `;
}
