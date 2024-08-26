import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit{
  userForm: FormGroup = this.fb.group({});
  userService: UserService;
  isEditMode: boolean = false;
  updateUser?: User;
  id?: number;


  constructor(private fb: FormBuilder, userService: UserService, 
    private route: ActivatedRoute, private router: Router, private snackbar: MatSnackBar) {
    this.userService = userService;
  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      userName: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });

    this.route.params.subscribe(param => {
      if(param['id']) {
        this.id = Number(param['id']);
        this.userService.getUserById(this.id).subscribe({
          next: (user: User) => {
            this.userForm.patchValue(user);
          },
          error: (e) => console.log(e),
          complete: () => console.info('complete')
        })
      
      }
    })
    

  }

  onSubmit(): void {
    this.userService.createUser(this.userForm.value)
    .subscribe({
      next: (v) => {
        this.snackbar.open(v.userName + ' created!', 'Close', {duration: 3000} );
      },
      error: (e) => {
        this.snackbar.open(e.error, 'Close', {duration: 5000} );
      },
      complete: () => this.router.navigate(['/'])
    });
    
  }

  cancel():void {
    this.router.navigate(['/']);
  }

}
