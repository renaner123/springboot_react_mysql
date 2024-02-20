import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import './styles.css';

import api from '../../services/api';

import logoImage from '../../asserts/logo.svg';

export default function Books() {
    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');
    const [books, setBooks] = useState([]);
    const navigate = useNavigate();

    async function handleLogout() {
        localStorage.clear();
        navigate('/');
    }

    async function handleDeleteBook(id) {
        try {
            await api.delete(`api/book/v1/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

            setBooks(books.filter(book => book.id !== id));
        } catch (err) {
            alert('Error deleting book, try again.');
        }
    }

    async function handleEditBook(id) {
        try {
            navigate(`/books/new/${id}`);
        } catch (err) {
            alert('Edit failed!, try again.');
        }
    }

    useEffect(() => {
        api.get('api/book/v1', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            params:
            {
                page: 1,
                size: 4,
                direction: 'asc'
            }
        }).then(response => {
            setBooks(response.data._embedded.bookVOList)
            console.log(response.data);
        })
    }, []);

    return (

        <div className="book-container">
            <header>
                <img src={logoImage} alt="Application logo" />
                <span>Welcome, <strong>{username.toUpperCase()}</strong> </span>
                <Link className="button" to="/books/new/0">Add new Book</Link>
                <button onClick={() => handleLogout()} type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>

            <h1>Registered Books</h1>
            <ul>
                {books.map(book => (
                    <li key={book.id}>
                        <strong>Title:</strong>
                        <p>{book.title}</p>

                        <strong>Author:</strong>
                        <p>{book.author}</p>

                        <strong>Price:</strong>
                        <p>{Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(book.price)}</p>

                        <strong>Release Date:</strong>
                        <p>{Intl.DateTimeFormat('pt-BR').format(book.release_date)}</p>

                        <button onClick={() => handleEditBook(book.id)} type="button">
                            <FiEdit size={20} color="#251FC5" />
                        </button>

                        <button onClick={() => handleDeleteBook(book.id)} type="button">
                            <FiTrash2 size={20} color="#251FC5" />
                        </button>
                    </li>
                ))}
            </ul>
        </div>

    );
}