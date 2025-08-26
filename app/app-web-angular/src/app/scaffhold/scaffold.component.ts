import {Component} from '@angular/core';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {RouterOutlet} from '@angular/router';

@Component({
    selector: 'app-scaffold',
    imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatButtonModule, RouterOutlet],
    templateUrl: './scaffold.component.html',
    styleUrl: './scaffold.component.scss'
})
export class ScaffoldComponent {

}
