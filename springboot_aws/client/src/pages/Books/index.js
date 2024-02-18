import React from 'react';
import { Link } from 'react-router-dom';
import { FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import './styles.css';

import logoImage from '../../asserts/logo.svg';

export default function Books() { 
    return (

        <div className="book-container">
            <header>
                <img src={logoImage} alt="Application logo" />
                <span>Welcome, <strong>Renan</strong> </span>
                <Link className="button" to="/books/new">Add new Book</Link>
                <button type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>

            <h1>Registered Books</h1>
            <ul>
                <li>
                    <strong>Title:</strong>
                    <p>React Native</p>

                    <strong>Author:</strong>
                    <p>Renan</p>

                    <strong>Price:</strong>
                    <p>$ 120.00</p>

                    <button type="button">
                        <FiEdit size={20} color="#251FC5" />
                    </button>
                    
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5" />
                    </button>                
                </li>

                <li>
                    <strong>Title:</strong>
                    <p>React Native</p>

                    <strong>Author:</strong>
                    <p>Renan</p>

                    <strong>Price:</strong>
                    <p>$ 120.00</p>

                    <button type="button">
                        <FiEdit size={20} color="#251FC5" />
                    </button>
                    
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5" />
                    </button>                
                </li>

                <li>
                    <strong>Title:</strong>
                    <p>React Native</p>

                    <strong>Author:</strong>
                    <p>Renan</p>

                    <strong>Price:</strong>
                    <p>$ 120.00</p>

                    <button type="button">
                        <FiEdit size={20} color="#251FC5" />
                    </button>
                    
                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5" />
                    </button>                
                </li>
            
            </ul>
        </div>

    );
}