import { Component, OnInit, Renderer2 } from '@angular/core';
import { TokenStorageService } from '../services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  info: any;

  constructor(private token: TokenStorageService, private renderer: Renderer2) {
    this.setBackground();
   }

  ngOnInit() {
    this.info = {
      token: this.token.getToken(),
      username: this.token.getUsername(),
      authorities: this.token.getAuthorities()
    };
  }

  logout() {
    this.token.signOut();
    window.location.reload();
  }

  setBackground() {
    this.renderer.setStyle(document.body, 'background-image', 'url("/assets/images/home-background.jpg")');
    this.renderer.setStyle(document.body, 'webkit-background-size', 'cover');
    this.renderer.setStyle(document.body, '-moz-background-size', 'cover');
    this.renderer.setStyle(document.body, '-o-background-size', 'cover');
    this.renderer.setStyle(document.body, 'background-size', 'cover');
  }

}
