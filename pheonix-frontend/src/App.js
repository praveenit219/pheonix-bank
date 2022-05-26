import React, { Component } from 'react';
import './App.css';
import BankApp from './component/BankApp.jsx'

class App extends Component {
  render() {
    return (
      <div className="container">
        <BankApp />
      </div>
    );
  }
}

export default App;
