import React from 'react';
import './styles.css';

import logoImage from '../../asserts/logo.svg';
import padlock from '../../asserts/padlock.png';

export default function Login() { 
    return (
        <div className="login-container">  
            <section className="form">
                <img src={logoImage} alt="Be The Hero"/>
                <form>
                    <h1>Access your account</h1>
                    <input placeholder="Username"/>
                    <input type="password" placeholder="Password"/>
                    
                    
                    <button className="button" type="submit">Login</button>
                </form>
            </section>
        
            <img src={padlock} alt="Padlock"/>
        
        </div>
    );
}