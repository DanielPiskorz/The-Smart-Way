import { Component, OnInit } from '@angular/core';

import { AuthService } from '../services/auth-service.service';
import { TokenStorageService } from '../services/token-storage.service';
import { LoginData } from '../models/login';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private loginData: LoginData;

  constructor(private authService: AuthService,
     private tokenStorage: TokenStorageService,
     private router: Router) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getAuthorities();
    }
  }

  onSubmit() {
    console.log(this.form);

    this.loginData = new LoginData(
      this.form.username,
      this.form.password);

    this.authService.attemptAuth(this.loginData).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUsername(data.username);
        this.tokenStorage.saveAuthorities(data.authorities);
        this.tokenStorage.notifyAfterLoginSuccess();

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getAuthorities();
        this.redirectToProjectsPage();
      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  redirectToProjectsPage() {
    this.router.navigate(['/projects']);
  }
}
