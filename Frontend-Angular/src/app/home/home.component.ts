import { Component, OnInit, Renderer2 } from '@angular/core';
import { TokenStorageService } from '../services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  info: any;
  loggedIn = false;

  constructor(private token: TokenStorageService, private renderer: Renderer2) {
    this.token.loggedIn.subscribe( condition => {
      this.loggedIn = condition;
    });
  }

  ngOnInit() {
    this.info = {
      token: this.token.getToken(),
      username: this.token.getUsername(),
      authorities: this.token.getAuthorities()
    };
  }




}
