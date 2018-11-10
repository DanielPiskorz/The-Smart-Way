import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './services/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private roles: string[];
  private authority: string;

  constructor(private tokenStorage: TokenStorageService) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every(role => {

        switch (role) {
          case 'ROLE_ADMIN':
            this.authority = 'admin';
            return false;
          case 'ROLE_PM':
            this.authority = 'pm';
            return false;
          default:
            this.authority = 'user';
            return true;
        }
      });
    }
  }
}
