export interface ITransaction {
  id?: number;
  from_id?: number;
  to_id?: number;
  amount?: number;
}

export class Transaction implements ITransaction {
  constructor(public id?: number, public from_id?: number, public to_id?: number, public amount?: number) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
