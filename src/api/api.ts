import {
  Account,
  Debt,
  ExchangeRate,
  FamilyMember,
  Notification,
  Transaction,
  categoryIcons,
} from "../context/AppContext";

export interface ApiConfig {
  baseUrl: string;
}

let apiConfig: ApiConfig = {
  baseUrl: import.meta.env.VITE_API_BASE_URL || "",
};

export const setApiConfig = (config: Partial<ApiConfig>) => {
  apiConfig = { ...apiConfig, ...config };
};

export const getApiConfig = () => apiConfig;

type HttpMethod = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";

const apiFetch = async <T>(path: string, method: HttpMethod = "GET", body?: unknown): Promise<T> => {
  const response = await fetch(`${apiConfig.baseUrl}${path}`, {
    method,
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `HTTP ${response.status}`);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
};

let authReadyPromise: Promise<void> | null = null;

const ensureSession = async (): Promise<void> => {
  if (authReadyPromise) return authReadyPromise;

  authReadyPromise = (async () => {
    try {
      await apiFetch("/api/auth/me");
      return;
    } catch {
      const username = import.meta.env.VITE_DEMO_USERNAME || "demo";
      const password = import.meta.env.VITE_DEMO_PASSWORD || "demo123";

      try {
        await apiFetch("/api/auth/register", "POST", { username, password });
      } catch {
        // might already exist
      }

      await apiFetch("/api/auth/login", "POST", { username, password });
    }
  })();

  return authReadyPromise;
};

const parseCardNetwork = (value?: string): Account["cardNetwork"] => {
  if (!value) return "none";
  if (value === "visa" || value === "mastercard" || value === "humo" || value === "uzcard") return value;
  return "none";
};

const accountToUi = (account: any): Account => ({
  id: String(account.id),
  name: account.name,
  type: account.type,
  currency: account.currency,
  balance: Number(account.balance ?? 0),
  color: account.color || "from-slate-500 to-slate-600",
  cardNetwork: parseCardNetwork(account.cardNetwork),
  cardNumberFull: account.cardNumberFull,
  expiryDate: account.expiryDate,
  includedInBalance: account.includedInBalance,
});

const accountToBackend = (account: Partial<Account>) => ({
  name: account.name,
  type: account.type,
  currency: account.currency,
  balance: account.balance,
  color: account.color,
  cardNetwork: account.cardNetwork,
  cardNumberMasked: undefined,
  cardNumberFull: account.cardNumberFull,
  expiryDate: account.expiryDate,
  includedInBalance: account.includedInBalance,
});

const transactionToUi = (transaction: any, accountById: Map<string, Account>): Transaction => {
  const accountId = String(transaction.accountId);
  const toAccountId = transaction.toAccountId ? String(transaction.toAccountId) : undefined;

  return {
    id: String(transaction.id),
    type: transaction.type,
    amount: Number(transaction.amount ?? 0),
    currency: transaction.currency,
    category: transaction.category,
    description: transaction.description,
    accountId,
    accountName: accountById.get(accountId)?.name || "Unknown account",
    toAccountId,
    toAccountName: toAccountId ? accountById.get(toAccountId)?.name : undefined,
    toCurrency: transaction.toCurrency,
    toAmount: transaction.toAmount != null ? Number(transaction.toAmount) : undefined,
    date: transaction.occurredAt,
    icon: categoryIcons[transaction.category] || "circle-dot",
    note: transaction.note,
  };
};

const transactionToBackend = (transaction: Partial<Transaction>) => ({
  type: transaction.type,
  amount: transaction.amount,
  currency: transaction.currency,
  category: transaction.category,
  description: transaction.description,
  note: transaction.note,
  accountId: Number(transaction.accountId),
  toAccountId: transaction.toAccountId ? Number(transaction.toAccountId) : null,
  toCurrency: transaction.toCurrency,
  toAmount: transaction.toAmount,
  occurredAt: transaction.date,
});

const debtToUi = (debt: any): Debt => ({
  id: String(debt.id),
  name: debt.name,
  amount: Number(debt.amount ?? 0),
  currency: debt.currency,
  type: debt.type,
  status: debt.status,
  date: debt.debtDate,
  description: debt.description,
});

const debtToBackend = (debt: Partial<Debt>) => ({
  name: debt.name,
  amount: debt.amount,
  currency: debt.currency,
  type: debt.type,
  status: debt.status,
  debtDate: debt.date,
  description: debt.description,
});

const notificationToUi = (notification: any): Notification => ({
  id: String(notification.id),
  title: notification.title,
  message: notification.message,
  date: notification.createdAt,
  read: Boolean(notification.read),
  type: notification.type,
});

const exchangeRateToUi = (rate: any): ExchangeRate => ({
  from: rate.fromCurrency,
  to: rate.toCurrency,
  rate: Number(rate.rate ?? 0),
});

export const accountsApi = {
  async getAllAccounts(): Promise<Account[]> {
    await ensureSession();
    const data = await apiFetch<any[]>("/api/accounts");
    return data.map(accountToUi);
  },

  async getAccountById(id: string): Promise<Account | undefined> {
    await ensureSession();
    const data = await apiFetch<any>(`/api/accounts/${id}`);
    return accountToUi(data);
  },

  async createAccount(account: Omit<Account, "id">): Promise<Account> {
    await ensureSession();
    const created = await apiFetch<any>("/api/accounts", "POST", accountToBackend(account));
    return accountToUi(created);
  },

  async updateAccount(id: string, updates: Partial<Account>): Promise<Account> {
    await ensureSession();
    const updated = await apiFetch<any>(`/api/accounts/${id}`, "PUT", accountToBackend(updates));
    return accountToUi(updated);
  },

  async deleteAccount(id: string): Promise<void> {
    await ensureSession();
    await apiFetch<void>(`/api/accounts/${id}`, "DELETE");
  },
};

export const transactionsApi = {
  async getAllTransactions(): Promise<Transaction[]> {
    await ensureSession();
    const [accounts, transactions] = await Promise.all([
      accountsApi.getAllAccounts(),
      apiFetch<any[]>("/api/transactions"),
    ]);
    const accountMap = new Map(accounts.map((a) => [a.id, a]));
    return transactions.map((tx) => transactionToUi(tx, accountMap));
  },

  async getTransactionsByAccount(accountId: string): Promise<Transaction[]> {
    const all = await this.getAllTransactions();
    return all.filter((transaction) => transaction.accountId === accountId);
  },

  async createTransaction(transaction: Omit<Transaction, "id">): Promise<Transaction> {
    await ensureSession();
    const [created, accounts] = await Promise.all([
      apiFetch<any>("/api/transactions", "POST", transactionToBackend(transaction)),
      accountsApi.getAllAccounts(),
    ]);
    const accountMap = new Map(accounts.map((a) => [a.id, a]));
    return transactionToUi(created, accountMap);
  },

  async updateTransaction(id: string, updates: Partial<Transaction>): Promise<Transaction> {
    await ensureSession();
    const existing = await apiFetch<any>(`/api/transactions/${id}`);
    const payload = transactionToBackend({
      ...transactionToUi(existing, new Map()),
      ...updates,
    });
    const [updated, accounts] = await Promise.all([
      apiFetch<any>(`/api/transactions/${id}`, "PUT", payload),
      accountsApi.getAllAccounts(),
    ]);
    return transactionToUi(updated, new Map(accounts.map((a) => [a.id, a])));
  },

  async deleteTransaction(id: string): Promise<void> {
    await ensureSession();
    await apiFetch<void>(`/api/transactions/${id}`, "DELETE");
  },
};

export const debtsApi = {
  async getAllDebts(): Promise<Debt[]> {
    await ensureSession();
    const debts = await apiFetch<any[]>("/api/debts");
    return debts.map(debtToUi);
  },

  async createDebt(debt: Omit<Debt, "id">): Promise<Debt> {
    await ensureSession();
    const created = await apiFetch<any>("/api/debts", "POST", debtToBackend(debt));
    return debtToUi(created);
  },

  async updateDebt(id: string, updates: Partial<Debt>): Promise<Debt> {
    await ensureSession();
    const existing = await apiFetch<any>(`/api/debts`);
    const current = existing.find((d: any) => String(d.id) === id);
    const updated = await apiFetch<any>(`/api/debts/${id}`, "PUT", debtToBackend({ ...debtToUi(current), ...updates }));
    return debtToUi(updated);
  },

  async deleteDebt(id: string): Promise<void> {
    await ensureSession();
    await apiFetch<void>(`/api/debts/${id}`, "DELETE");
  },
};

export const notificationsApi = {
  async getAllNotifications(): Promise<Notification[]> {
    await ensureSession();
    const notifications = await apiFetch<any[]>("/api/notifications");
    return notifications.map(notificationToUi);
  },

  async createNotification(notification: Omit<Notification, "id">): Promise<Notification> {
    return {
      ...notification,
      id: String(Date.now()),
    };
  },

  async deleteNotification(id: string): Promise<void> {
    return;
  },
};

export const familyMembersApi = {
  async getAllFamilyMembers(): Promise<FamilyMember[]> {
    return [];
  },
  async createFamilyMember(member: Omit<FamilyMember, "id">): Promise<FamilyMember> {
    return { ...member, id: String(Date.now()) };
  },
  async updateFamilyMember(id: string, updates: Partial<FamilyMember>): Promise<FamilyMember> {
    return {
      id,
      name: updates.name || "Member",
      email: updates.email || "",
      avatar: updates.avatar || "👤",
      role: updates.role || "member",
    };
  },
  async deleteFamilyMember(): Promise<void> {
    return;
  },
};

export const exchangeRatesApi = {
  async getExchangeRates(): Promise<ExchangeRate[]> {
    await ensureSession();
    const rates = await apiFetch<any[]>("/api/exchange-rates");
    return rates.map(exchangeRateToUi);
  },

  async updateExchangeRate(from: string, to: string, rate: number): Promise<ExchangeRate> {
    await ensureSession();
    const updated = await apiFetch<any>(`/api/exchange-rates/${from}/${to}?rate=${rate}`, "PUT");
    return exchangeRateToUi(updated);
  },
};

export const api = {
  accounts: accountsApi,
  transactions: transactionsApi,
  debts: debtsApi,
  notifications: notificationsApi,
  familyMembers: familyMembersApi,
  exchangeRates: exchangeRatesApi,
  config: {
    set: setApiConfig,
    get: getApiConfig,
  },
};

export default api;
