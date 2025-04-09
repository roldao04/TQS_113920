-- Insert restaurants with more detailed information
INSERT INTO restaurants (name, address, description, phone_number) VALUES
('Tasca do Moliceiro', 'Rua dos Mercadores 44, Aveiro', 'Traditional Portuguese restaurant specializing in seafood and local delicacies', '234-378-367'),
('Café Bistro', 'Praça do Peixe 12, Aveiro', 'Modern fusion cuisine with a focus on light meals and great coffee', '234-421-580'),
('O Bairro', 'Rua da Universidade 23, Aveiro', 'Student-friendly restaurant offering daily specials and comfort food', '234-890-123');

-- Insert meals for each restaurant for the next two weeks
-- Tasca do Moliceiro (Restaurant 1)
INSERT INTO meals (name, description, price, category, restaurant_id) VALUES
-- Week 1
('Bacalhau à Lagareiro', 'Grilled cod with olive oil and roasted potatoes', 18.50, 'Fish', 1),
('Polvo à Lagareiro', 'Grilled octopus with olive oil and sweet potato', 22.00, 'Fish', 1),
('Vitela Assada', 'Roasted veal with rice and vegetables', 16.50, 'Meat', 1),
('Arroz de Marisco', 'Seafood rice with shrimp and mussels', 19.90, 'Fish', 1),
('Caldeirada de Peixe', 'Traditional fish stew', 17.50, 'Fish', 1),
-- Week 2
('Secretos de Porco Preto', 'Black pork secrets with sweet potato puree', 15.90, 'Meat', 1),
('Robalo Grelhado', 'Grilled sea bass with vegetables', 21.00, 'Fish', 1),
('Arroz de Tamboril', 'Monkfish rice with prawns', 23.50, 'Fish', 1),
('Bife à Portuguesa', 'Portuguese style steak with egg and fries', 17.90, 'Meat', 1),
('Dourada Grelhada', 'Grilled sea bream with boiled potatoes', 16.50, 'Fish', 1),

-- Café Bistro (Restaurant 2)
-- Week 1
('Salada Caesar', 'Classic Caesar salad with grilled chicken', 11.90, 'Salad', 2),
('Wrap Vegetariano', 'Vegetarian wrap with hummus and grilled vegetables', 9.50, 'Vegetarian', 2),
('Penne al Pesto', 'Penne pasta with homemade pesto sauce', 12.50, 'Pasta', 2),
('Quiche Lorraine', 'Classic quiche with bacon and cheese', 10.90, 'Snack', 2),
('Bowl de Quinoa', 'Quinoa bowl with avocado and grilled chicken', 13.50, 'Healthy', 2),
-- Week 2
('Bagel de Salmão', 'Smoked salmon bagel with cream cheese', 11.90, 'Snack', 2),
('Salada Mediterrânea', 'Mediterranean salad with feta cheese', 10.50, 'Salad', 2),
('Tosta de Abacate', 'Avocado toast with poached egg', 9.90, 'Breakfast', 2),
('Wrap de Atum', 'Tuna wrap with mixed vegetables', 10.90, 'Fish', 2),
('Pasta Primavera', 'Vegetarian pasta with seasonal vegetables', 12.90, 'Vegetarian', 2),

-- O Bairro (Restaurant 3)
-- Week 1
('Bitoque', 'Steak with egg, rice and fries', 8.90, 'Meat', 3),
('Francesinha', 'Traditional Porto sandwich with special sauce', 9.90, 'Snack', 3),
('Hambúrguer do Chef', 'Chef\s special burger with fries', 10.50, 'Meat', 3),
('Massa à Bolonhesa', 'Spaghetti bolognese', 7.90, 'Pasta', 3),
('Prato Vegetariano', 'Daily vegetarian special', 8.50, 'Vegetarian', 3),
-- Week 2
('Frango no Churrasco', 'Grilled chicken with rice and fries', 8.90, 'Meat', 3),
('Bifana no Pão', 'Traditional pork sandwich', 5.90, 'Snack', 3),
('Alheira com Ovo', 'Traditional Portuguese sausage with egg', 9.50, 'Meat', 3),
('Salada de Atum', 'Tuna salad with mixed vegetables', 7.90, 'Fish', 3),
('Pizza Margherita', 'Classic margherita pizza', 8.90, 'Pizza', 3); 