import React, { Component } from 'react';
import MenuComponent from './MenuComponent';
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent ';
import PaymentComponent from './PaymentComponent';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import AuthenticatedRoute from './AuthenticatedRoute';

class BankApp extends Component {


    render() {
        return (
            <>
                
                    <>
                        <MenuComponent />
                        <Switch>
                            <Route path="/" exact component={LoginComponent} />
                            <Route path="/login" exact component={LoginComponent} />
                            <AuthenticatedRoute path="/logout" exact component={LogoutComponent} />
                            <AuthenticatedRoute path="/payments" exact component={PaymentComponent} />
                        </Switch>
                    </>
                
            </>
        )
    }
}

export default BankApp