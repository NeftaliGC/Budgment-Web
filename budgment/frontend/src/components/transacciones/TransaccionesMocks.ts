export type Transaction = {
  id: string;
  title: string;
  date: string;
  amount: string;
  category: string;
};

export type Budget = {
  id: string;
  name: string;
  limit: string;
  date: string;
  percent: number;
};

export const TRANSACTIONS_MOCK: Transaction[] = [
  {
    id: "t1",
    title: "Transaccion 1",
    date: "12/12/12",
    amount: "15,347.89",
    category: "Categoria 1",
  },
  {
    id: "t2",
    title: "Transaccion 2",
    date: "12/11/12",
    amount: "5,120.00",
    category: "Categoria 2",
  },
  {
    id: "t3",
    title: "Transaccion 3",
    date: "12/10/12",
    amount: "2,300.50",
    category: "Categoria 1",
  },
  {
    id: "t4",
    title: "Transaccion 4",
    date: "12/09/12",
    amount: "850.00",
    category: "Categoria 3",
  },
  {
    id: "t5",
    title: "Transaccion 5",
    date: "12/08/12",
    amount: "120.00",
    category: "Categoria 1",
  },
  {
    id: "t6",
    title: "Transaccion 6",
    date: "12/07/12",
    amount: "45.00",
    category: "Categoria 2",
  },
  {
    id: "t7",
    title: "Transaccion 7",
    date: "12/06/12",
    amount: "900.00",
    category: "Categoria 1",
  },
];

export const BUDGETS_MOCK: Budget[] = [
  {
    id: "b1",
    name: "Presupuesto 1",
    limit: "15,347.89",
    date: "12/12/12",
    percent: 20,
  },
  {
    id: "b2",
    name: "Presupuesto 2",
    limit: "15,347.89",
    date: "12/12/12",
    percent: 20,
  },
  {
    id: "b3",
    name: "Presupuesto 3",
    limit: "15,347.89",
    date: "12/12/12",
    percent: 20,
  },
];