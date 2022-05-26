import React, { Component } from 'react'
import PaymentService from '../service/PaymentService.js';
import AuthenticationService from '../service/AuthenticationService.js';

class PaymentComponent extends Component {
    
    constructor(props) {
        super(props)

        this.state = {
            user: AuthenticationService.getLoggedInUserName(),
            amount: '',
            balance: '',
            receiver : '',
            transferMoney: '',
            sender: AuthenticationService.getLoggedInUserName(),
            availableBalance: '',
            transferAmount: '',
            message: '',
            topupMessage:''
        }
        this.handleChange = this.handleChange.bind(this)
        this.topupClicked = this.topupClicked.bind(this)
        this.transferClicked = this.transferClicked.bind(this)
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }

    topupClicked() {
        console.log('topupClicked');
        PaymentService
            .executeTopupTransaction(this.state.user, this.state.amount)
            .then(response => {
                this.setState({ balance: response.data.balance })
                this.setState({ topupMessage: response.data.message })
            }).catch(() => {
                
            })       
    }

    transferClicked() {
        console.log('transferClicked');
        PaymentService
            .executeAnotherClientTransaction(this.state.sender, this.state.receiver, this.state.transferMoney)
            .then(response => {
                this.setState({ sender: response.data.sender })
                this.setState({ receiver: response.data.receiver })
                this.setState({ availableBalance: response.data.availableBalance })
                this.setState({ transferAmount: response.data.transferAmount })
                this.setState({ message: response.data.message })
            }).catch(() => {
                
            })    
    }

    render() {
        return (
            <div>
                <h1>TOP UP Payments</h1>
                topup {this.state.balance} <br></br>
                your balance is <b>{this.state.balance}</b><br></br>{this.state.topupMessage}<br></br>
                User: <input type="text" name="user" value={this.state.user} onChange={this.handleChange} />
                Topup: <input type="text" name="amount" value={this.state.amount} onChange={this.handleChange} />
                <button className="btn btn-success" onClick={this.topupClicked}>Topup</button>
                <hr></hr>

                <h1>TRANSFER Payments</h1>
                
                Transferred {this.state.transferMoney} to {this.state.receiver}<br></br>
                your balance is {this.state.availableBalance}<br></br>{this.state.message}<br></br>
                transfer to: <input type="text" name="receiver" value={this.state.receiver} onChange={this.handleChange} />
                amount: <input type="text" name="transferMoney" value={this.state.transferMoney} onChange={this.handleChange} />
                <button className="btn btn-success" onClick={this.transferClicked}>Transfer</button>
            </div>
              
        )
    }

}

export default PaymentComponent