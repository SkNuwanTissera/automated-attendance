import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';
import {StudentRegisterComponent} from './student-register/student-register.component';
import {HttpClientModule} from '@angular/common/http';
import {MarkAttendanceComponent} from "./mark-attendance/mark-attendance.component";
import {StudentAttendaceTableComponent} from "./student-attendace-table/student-attendace-table.component";
import {StudentListComponent} from "./student-list/student-list.component";

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'prefix' },
            { path: 'dashboard', loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule) },
            { path: 'charts', loadChildren: () => import('./charts/charts.module').then(m => m.ChartsModule) },
            { path: 'tables', loadChildren: () => import('./tables/tables.module').then(m => m.TablesModule) },
            { path: 'forms', loadChildren: () => import('./form/form.module').then(m => m.FormModule) },
            { path: 'bs-element', loadChildren: () => import('./bs-element/bs-element.module').then(m => m.BsElementModule) },
            { path: 'grid', loadChildren: () => import('./grid/grid.module').then(m => m.GridModule) },
            { path: 'components', loadChildren: () => import('./bs-component/bs-component.module').then(m => m.BsComponentModule) },
            { path: 'blank-page', loadChildren: () => import('./blank-page/blank-page.module').then(m => m.BlankPageModule) },
            {path: 'student-register', component: StudentRegisterComponent},
            {
                path:'mark-attendance',
                component: MarkAttendanceComponent,
            },
            {
                path:'student-attendance-table',
                component:StudentAttendaceTableComponent,

            },
            {
                path:'student-table',
                component:StudentListComponent
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes), HttpClientModule],
    exports: [RouterModule]
})
export class LayoutRoutingModule {}
