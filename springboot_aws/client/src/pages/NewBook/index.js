import React, {useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiArrowLeft, FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import './styles.css';

import api from '../../services/api';

import logoImage from '../../asserts/logo.svg';

export default function NewBook() {
    const [id, setid] = useState('');
    const [author, setAuthor] = useState('');
    const [launch_date, setLaunchDate] = useState('');
    const [price, setPrice] = useState('');
    const [title, setTitle] = useState('');

    const navigate = useNavigate();

    async function createNewBook(e) {
        e.preventDefault();

        const data = {
            title,
            author,
            launch_date, 
            price
        };

        const username = localStorage.getItem('username');
        const accessToken = localStorage.getItem('accessToken');

        try {
            await api.post('api/book/v1', data, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            
            });
            navigate('/books');
        } catch (error) {
            console.error('Error:', error.message); // Log the error message
          }
    };
    

    return(
        <div className="new-book-container">
            <div className="content">
                <section className="form">
                    <img src={logoImage} alt="Renan" />
                    <h1>Add new Book</h1>
                    <p>Enter the book information and click on 'Add'!</p>
                    <Link className="back-link" to="/books">
                        <FiArrowLeft size={16} color="#251fC5" />
                        Home
                    </Link>
                </section>
                <form onSubmit={createNewBook}>
                    <input 
                        placeholder="Title" 
                        value={title}
                        onChange={e => setTitle(e.target.value)}                    
                    />
                    <input 
                        placeholder="Author" 
                        value={author}
                        onChange={e => setAuthor(e.target.value)}                    
                    />
                    <input 
                        type="date"
                        value={launch_date}
                        onChange={e => setLaunchDate(e.target.value)}                    
                    />
                    <input 
                        placeholder="Price" 
                        value={price}
                        onChange={e => setPrice(e.target.value)}                    
                    />

                    <button className="button" type="submit">Add</button>
                </form>
            </div>
        </div>
    );

}