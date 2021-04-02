import { TransactionInfo } from 'app/entities/monopolist/transaction-info.model';

export interface IMonopolist {
  id?: number;
  name?: string;
  balance?: number;
  isBank?: boolean;
  bank?: IMonopolist | null;
  transactions?: TransactionInfo[];
}

export class Monopolist implements IMonopolist {
  constructor(
    public id?: number,
    public name?: string,
    public balance?: number,
    public isBank?: boolean,
    public transactions?: TransactionInfo[]
  ) {
    this.isBank = this.isBank ?? false;
  }
}
