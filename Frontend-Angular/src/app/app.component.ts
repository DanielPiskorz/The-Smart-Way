import { Component, OnInit, Renderer2 } from '@angular/core';
import { TokenStorageService } from './services/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  private roles: string[];
  private authority: string;
  private loggedIn = false;

  constructor(private tokenStorage: TokenStorageService, private renderer: Renderer2, private router: Router) {
    tokenStorage.loggedIn.subscribe( condition => {
      this.loggedIn = condition;
    });
  }

  ngOnInit() {
    this.setBackground();
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

  logout() {
    this.tokenStorage.signOut();
    this.router.navigate(['/home']);
  }

  setBackground() {
    this.renderer.setStyle(document.body, 'background-image', 'url("/assets/images/home-background.jpg")');
    this.renderer.setStyle(document.body, 'webkit-background-size', 'cover');
    this.renderer.setStyle(document.body, '-moz-background-size', 'cover');
    this.renderer.setStyle(document.body, '-o-background-size', 'cover');
    this.renderer.setStyle(document.body, 'background-size', 'cover');
    this.renderer.setStyle(document.body, 'background-attachment', 'fixed');
  }
}
