import axios from 'axios'

const API_URL = 'http://localhost:11022'

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'
class PaymentService {

    executeTopupTransaction(user, amount) {
        console.log(user);
        return axios.post(`${API_URL}/topup`, {
            user,
            amount
        })
    }

    executeAnotherClientTransaction(sender, receiver, amount) {
        console.log(sender);
        return axios.post(`${API_URL}/pay`, {
            sender,
            receiver,
            amount
        })
    }

}

export default new PaymentService()