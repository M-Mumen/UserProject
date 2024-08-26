export class User {
    id: number;
    userName: string;
    firstName: string;
    lastName: string;
    email: string;
  
    constructor(id: number, userName: string, firstName: string, lastName: string, email: string) {
      this.id = id;
      this.userName = userName;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
    }
  }
  