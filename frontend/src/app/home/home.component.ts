import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { MatDialog } from '@angular/material/dialog';
import { DeleteUserComponent } from '../delete-user/delete-user.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  users: User[] = [];
  displayedColumns: string[] = ['id', 'userName', 'firstName', 'lastName', 'email', 'actions'];

  constructor(private userService: UserService, 
    private dialog: MatDialog, private snackbar: MatSnackBar, private router: Router) {}
  
  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getUsers().subscribe((data: User[]) => {
      this.users = data;
      console.log(data);
    });
  }

  openDeleteDialog(userId: number): void {
    const dialogRef = this.dialog.open(DeleteUserComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Perform the delete action
        console.log(userId);
        this.deleteItem(userId);
      }
    });
  }

  openEditForm(userId: number) {
    console.log(userId);
    this.router.navigate(['add-user', {id: userId}]);
  }

  deleteItem(userId: number) {
    // Logic to delete the item
    this.userService.deleteUser(userId).subscribe(() => {
      console.log('Item deleted');
      this.snackbar.open('User deleted successfully', 'Close', {duration: 3000});
      this.getUsers();
      
    });
  }

}
