import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { SignalementFormComponent } from './signalement-form/signalement-form.component';
import { ActionsPageComponent } from './actions-page/actions-page.component';
import { CompteRenduComponent } from './compte-rendu/compte-rendu.component';
import { NouvelleCriseComponent } from './nouvelle-crise/nouvelle-crise.component';
import { SignalementComponent } from './signalement-form/signalement.component';
import { ActionsComponent } from './actions/actions.component';
import { HistoriqueComponent } from './historique/historique.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'signalement', component: SignalementComponent },
  { path: 'actions/:signalementId', component: ActionsComponent },
  { path: 'compte-rendu/:signalementId', component:CompteRenduComponent },
  { path: 'nouvelle-crise', component: NouvelleCriseComponent },
  { path: 'historique', component: HistoriqueComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
