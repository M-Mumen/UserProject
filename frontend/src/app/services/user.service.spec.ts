import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../models/user';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ 
        UserService, 
        { provide: 'url', 
          useValue: 'apiurl'
        }],
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => { 
    httpMock.verify(); 
   }); 

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve users from the API via GET', () => {
    const dummyUsers: User[] = [
      { id: 1, userName: 'Ironman', firstName: 'Tony', lastName: 'Stark', email: 'ironman@avengers.com' },
      { id: 2, userName: 'Cap', firstName: 'Steve', lastName: 'Rogers', email: 'cap@avengers.com' },
    ];

    service.getUsers().subscribe(users => {
      expect(users.length).toBe(2);
      expect(users).toEqual(dummyUsers);
    });

    const req = httpMock.expectOne(service['apiUrl']);
    expect(req.request.method).toBe('GET');
    req.flush(dummyUsers);
  });

});
