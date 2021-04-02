export class TransactionInfo {
  constructor(
    public from_id?: number,
    public to_id?: number,
    public from_name?: string,
    public to_name?: string,
    public amount?: number | null
  ) {}
}
