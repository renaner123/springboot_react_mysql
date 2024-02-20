import React, {useState, useEffect} from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { FiArrowLeft, FiEdit, FiPower, FiTrash2 } from 'react-icons/fi';
import './styles.css';

import api from '../../services/api';

import logoImage from '../../asserts/logo.svg';

export default function NewBook() {
    const [id, setId] = useState('');
    const [author, setAuthor] = useState('');
    const [launch_date, setLaunchDate] = useState('');
    const [price, setPrice] = useState('');
    const [title, setTitle] = useState('');
    const accessToken = localStorage.getItem('accessToken');

    const {bookId} = useParams();

    const navigate = useNavigate();

    async function loadBook(){
        try{
            const response = await api.get(`api/book/v1/${bookId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            
            });
            let adjustedDate = response.data.launch_date.split('T', 10)[0];

            setId(response.data.id);
            setTitle(response.data.title);
            setAuthor(response.data.author);
            setLaunchDate(adjustedDate);
            setPrice(response.data.price);

        }catch(error){
            console.error('Error:', error.message);
            navigate('/books');
        }
    }

    useEffect(() => {
        if (bookId !== '0') {
            loadBook(); // Call the loadBook function when bookId is not '0'
        }
    }, [bookId]); // Depend on bookId
    

    async function createNewBook(e) {
        e.preventDefault();

        const data = {
            title,
            author,
            launch_date, 
            price,
        };

        try{
            if (bookId === '0') {
                await api.post(`api/book/v1`, data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                }); 
            } else{
                data.id = id;
                await api.put(`api/book/v1`, data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                navigate('/books');
            }               
        }catch(error){
            console.error('Error:', error.message);
        }

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
                    <h1>{bookId === '0' ? "Add new" : "Update"} Book</h1>
                    <p>Enter the book information and click on {bookId === '0' ? "Add" : "Update"}!</p>
                    <Link className="back-link" to="/books">
                        <FiArrowLeft size={16} color="#251fC5" />
                        Back to Book
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

                    <button className="button" type="submit">{bookId === '0' ? 'Add' : 'Update'}</button>
                </form>
            </div>
        </div>
    );

}