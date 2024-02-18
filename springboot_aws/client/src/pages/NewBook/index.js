import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowLeft, FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import './styles.css';

import logoImage from '../../asserts/logo.svg';


export default function NewBook() {

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
                <form action="">
                    <input placeholder="Title" />
                    <input placeholder="Author" />
                    <input placeholder="date" />
                    <input placeholder="Price" />

                    <button className="button" type="submit">Add</button>
                </form>
            </div>
        </div>
    );

}