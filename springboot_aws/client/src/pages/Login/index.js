import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './styles.css';


import logoImage from '../../asserts/logo.svg';
import padlock from '../../asserts/padlock.png';
import api from '../../services/api';

export default function Login() { 
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function handleLogin(e) {
        e.preventDefault();

        const data = {
            username,
            password,
        };

        try{
            const response = await api.post('auth/signin', data);

            localStorage.setItem('username', username);
            localStorage.setItem('accessToken', response.data.accessToken );

            navigate('/books');

        } catch(err){
            alert('Error, try again.');
        }
    };

    return (
        <div className="login-container">  
            <section className="form">
                <img src={logoImage} alt="Be The Hero"/>
                <form onSubmit={handleLogin}>
                    <h1>Access your account</h1>
                    <input 
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}    
                    />
                    <input type="password" 
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />                    
                    
                    <button className="button" type="submit">Login</button>
                </form>
            </section>
        
            <img src={padlock} alt="Padlock"/>
        
        </div>
    );
}