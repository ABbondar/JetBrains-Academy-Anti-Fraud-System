class Account {

    private long balance = 0;

    public synchronized boolean withdraw(long amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public synchronized void deposit(long amount) {
        this.balance += amount;
    }

    public synchronized long getBalance() {
        return balance;
    }
}